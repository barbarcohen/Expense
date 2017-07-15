package cz.rudypokorny.expense.service.impl;


import cz.rudypokorny.expense.entity.ExpenseFilter;
import cz.rudypokorny.expense.entity.Result;
import cz.rudypokorny.expense.entity.Rules;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.AccountCreator;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.service.IExpenseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@javax.transaction.Transactional
public class ExpenseServiceIntegrationTest {


    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private IExpenseService expenseService;

    @Test
    public void spend() throws Exception {
        Account expectedAccount = testEntityManager.persist(Account.named("account"));
        String expectedMessage = "message";
        Expense expectedExpense = Expense.newExpense(33.0).by(expectedAccount).noted(expectedMessage);

        Result<Expense> result = expenseService.spend(expectedExpense);

        assertFalse(result.isCompromised());

        testEntityManager.flush();
        testEntityManager.clear();

        Account freshAccount = testEntityManager.find(Account.class, expectedAccount.getId());
        assertEquals(1, freshAccount.getExpenses().size());

        Expense actualExpense = freshAccount.getExpenses().get(0);

        assertEquals(expectedExpense.getId(), actualExpense.getId());
        assertEquals(expectedExpense.getAmount(), actualExpense.getAmount());
        assertEquals(expectedExpense.getWhen(), actualExpense.getWhen());
        assertEquals(expectedMessage, actualExpense.getNote());

        assertEquals(expectedAccount.getId(), actualExpense.getAccount().getId());

        assertNotNull(expectedExpense.getUpdatedDate());
        assertNotNull(expectedExpense.getCreatedDate());

    }

    @Test
    public void spendForUnknownAccount() {
        Expense expense = Expense.newExpense(33.0).by(AccountCreator.create()).noted("message");

        Result<Expense> result = expenseService.spend(expense);
        assertTrue(result.isCompromised());
    }

    @Test
    public void testNewAccountNull(){
        Result<Account> result = expenseService.newAccount(null);
        assertTrue(result.isCompromised());

        assertTrue(result.rules().getExceptions().isEmpty());
        assertEquals(1, result.rules().getErrors().size());
        assertEquals(Rules.Messages.ENTITY_IS_NULL, result.rules().getErrors().get(0));
    }

    @Test
    public void testNewAccountEmpty(){
        Result<Account> result = expenseService.newAccount(AccountCreator.createEmpty());
        assertTrue(result.isCompromised());

        assertTrue(result.rules().getErrors().isEmpty());
        assertEquals(1, result.rules().getExceptions().size());
        assertEquals(ConstraintViolationException.class, result.rules().getExceptions().get(0).getClass());
    }

    @Test
    public void testNewAccount(){
        Account expectedAccount= Account.named("account sdkfj dsf");
        Result<Account> result = expenseService.newAccount(expectedAccount);

        assertTrue(result.isOk());

        assertTrue(result.rules().getErrors().isEmpty());
        assertTrue(result.rules().getExceptions().isEmpty());

        Account account = testEntityManager.find(Account.class, result.get().getId());
        assertEquals(expectedAccount.getName(), account.getName());
        assertNotNull(account.getCreatedDate());
        assertNotNull(account.getUpdatedDate());
    }

    @Test
    public void testNewAccountDuplicitName(){
        Account expectedAccount= Account.named("account sdkfj dsf");
        Result<Account> result = expenseService.newAccount(expectedAccount);
        testEntityManager.clear();

        assertTrue(result.isOk());

        assertTrue(result.rules().getErrors().isEmpty());
        assertTrue(result.rules().getExceptions().isEmpty());

        Account account = testEntityManager.find(Account.class, result.get().getId());
        assertEquals(expectedAccount.getName(), account.getName());


        Result<Account> secondAttempt = expenseService.newAccount(Account.named(expectedAccount.getName()));
        assertTrue(secondAttempt.isCompromised());
        assertTrue(secondAttempt.rules().getErrors().isEmpty());
        assertEquals(1, secondAttempt.rules().getExceptions().size());
        assertEquals(DataIntegrityViolationException.class, secondAttempt.rules().getExceptions().get(0).getClass());
    }

    //TODO uncomment when impemented
    //@Test
    public void testFindExpenseByFilterWithCategory() throws Exception {
        Category expectedCategory = Category.named("whaterver");
        Account expectedAccount = Account.named("accoutn");

        testEntityManager.persistAndFlush(expectedAccount);
        testEntityManager.persistAndFlush(expectedCategory);
        testEntityManager.persistFlushFind(Expense.newExpense(30.0).on(expectedCategory).by(expectedAccount));

        Result<Iterable<Expense>> result = expenseService.findExpenseByFilter(ExpenseFilter.forCategory(expectedCategory));

        assertTrue(result.isOk());
        Expense actualExpense = result.get().iterator().next();
        assertEquals(expectedCategory.getName(), actualExpense.getCategory().getName());
        assertEquals(expectedAccount.getName(), actualExpense.getAccount().getName());
    }

    @Test
    public void findExpenseByFilterNullFiler() throws Exception {
        Result<Iterable<Expense>> result = expenseService.findExpenseByFilter(null);

        assertTrue(result.isCompromised());
    }
}