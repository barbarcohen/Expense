package cz.rudypokorny.expense.service.impl;

import cz.rudypokorny.expense.entity.ExpenseFilter;
import cz.rudypokorny.expense.entity.Result;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Record;
import cz.rudypokorny.expense.model.User;
import cz.rudypokorny.expense.service.IExpenseService;
import cz.rudypokorny.util.DateUtil;
import cz.rudypokorny.util.SecurityContextTestUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@javax.transaction.Transactional
public class ExpenseServiceFilterIntegrationTestUtil {


    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private IExpenseService expenseService;

    private Account defaultAccount;
    private Category categoryFoo;
    private Category categoryBar;

    @Before
    public void setup() {
        User user = SecurityContextTestUtil.addToSecurityContext(User.create("some", "something"));
        testEntityManager.persistAndFlush(user);

        defaultAccount = Account.named("defaultAccount");
        categoryBar = Category.named("BAR");
        categoryFoo = Category.named("FOO");
        testEntityManager.persist(defaultAccount);
        testEntityManager.persist(categoryBar);
        testEntityManager.persistAndFlush(categoryFoo);
        testEntityManager.clear();
    }

    //FIXME what about mixxing account when saving_ investigate these errors for that
    @Test
    public void testFindExpenseByFilterWithCategory() throws Exception {
        Category expectedCategory = Category.named("whaterver");
        Account expectedAccount = Account.named("accoutn");
        testEntityManager.persist(expectedCategory);
        testEntityManager.persist(expectedAccount);
        testEntityManager.persist(Record.newExpense(30.0).on(expectedCategory).by(expectedAccount));

        Result<Iterable<Record>> result = expenseService.findExpenseByFilter(ExpenseFilter.create(expectedAccount).forCategory(expectedCategory));

        assertTrue(result.isOk());
        Record actualRecord = result.get().iterator().next();
        assertEquals(expectedCategory.getName(), actualRecord.getCategory().getName());
        assertEquals(expectedAccount.getName(), actualRecord.getAccount().getName());
    }

    @Test
    public void findExpenseByFilterNullFiler() throws Exception {
        Result<Iterable<Record>> result = expenseService.findExpenseByFilter(null);

        assertTrue(result.isCompromised());
    }

    @Test
    public void findExpenseByFilterForAccount() throws Exception {
        Account falseAccount = Account.named("falseAccount");
        testEntityManager.persist(falseAccount);

        Record expectedRecord = persistExpense(defaultAccount, 1.1, null, null, null);
        Record falseRecord = persistExpense(falseAccount, 2.2, null, null, null);

        Result<Iterable<Record>> result = expenseService.findExpenseByFilter(ExpenseFilter.create(defaultAccount));

        assertTrue(result.isOk());
        ArrayList<Record> list = (ArrayList<Record>) result.get();

        assertEquals(1, list.size());
        assertEquals(expectedRecord.getAmount(), list.get(0).getAmount());
        assertEquals(defaultAccount.getId(), list.get(0).getAccount().getId());
    }

    @Test
    public void findExpenseByFilterForCategory() throws Exception {
        Category expectedCategory = categoryBar;
        Category falseCategory = categoryFoo;
        Record expectedRecord = persistExpense(defaultAccount, 1.1, expectedCategory, null, null);
        Record falseRecord = persistExpense(defaultAccount, 2.2, falseCategory, null, null);
        Record anotherFalseRecord = persistExpense(defaultAccount, 3.3, null, null, null);

        Result<Iterable<Record>> result = expenseService.findExpenseByFilter(ExpenseFilter.create(defaultAccount).forCategory(expectedCategory));

        assertTrue(result.isOk());
        ArrayList<Record> list = (ArrayList<Record>) result.get();

        assertEquals(1, list.size());
        assertEquals(expectedRecord.getAmount(), list.get(0).getAmount());
        assertEquals(expectedCategory.getId(), list.get(0).getCategory().getId());
    }

    @Test
    public void findExpenseByFilteWithNote() {
        String expectedNote = "abrakadabra";

        Record expectedRecord = persistExpense(defaultAccount, 1.1, null, null, expectedNote);

        Result<Iterable<Record>> result = expenseService.findExpenseByFilter(ExpenseFilter.create(defaultAccount).withNote(expectedNote));

        assertTrue(result.isOk());
        ArrayList<Record> list = (ArrayList<Record>) result.get();

        assertEquals(1, list.size());
        assertEquals(expectedRecord.getAmount(), list.get(0).getAmount());
        assertEquals(expectedNote, list.get(0).getNote());
    }

    @Test
    @Ignore
    public void findExpenseByFilterDefaultDates() {
        Record inPast = persistExpense(defaultAccount, 10.1, null, ExpenseFilter.getDefaultDateFrom().minusSeconds(1).atZone(DateUtil.getCurrentTimeZone()), null);
        Record inFuture = persistExpense(defaultAccount, 11.1, null, ExpenseFilter.getDefaultDateTo().plusSeconds(1).atZone(DateUtil.getCurrentTimeZone()), null);
        ZonedDateTime expectedWhen = DateUtil.getCurrentDateTime();

        ExpenseFilter filter = ExpenseFilter.create(defaultAccount);

        Record expectedRecord = persistExpense(defaultAccount, 1.1, null, expectedWhen, null);

        Result<Iterable<Record>> result = expenseService.findExpenseByFilter(filter);
        assertTrue(result.isOk());
        ArrayList<Record> list = (ArrayList<Record>) result.get();

//TODO use ZonedDateTime always?
        assertEquals(1, list.size());

        assertEquals(expectedRecord.getId(), list.get(0).getId());
        assertEquals(expectedRecord.getAmount(), list.get(0).getAmount());

    }

    @Test
    public void findExpenseByFilterAmount() {
        Record above = persistExpense(defaultAccount, 1.001, null, null, null);
        Record below = persistExpense(defaultAccount, 0.999, null, null, null);
        Record expectedRecord = persistExpense(defaultAccount, 1.0, null, null, null);

        Result<Iterable<Record>> result = expenseService.findExpenseByFilter(ExpenseFilter.create(defaultAccount).amountMBetween(1.0, 1.0));
        assertTrue(result.isOk());
        ArrayList<Record> list = (ArrayList<Record>) result.get();

        assertEquals(1, list.size());

        assertEquals(expectedRecord.getId(), list.get(0).getId());
        assertEquals(expectedRecord.getAmount(), list.get(0).getAmount());

    }

    private Record persistExpense(Account account, Double amount, Category category, ZonedDateTime when, String note) {
        Record record = Record.newExpense(amount).by(account).on(category).noted(note).at(when);

        testEntityManager.persistAndFlush(record);
        testEntityManager.clear();
        return record;
    }
}
