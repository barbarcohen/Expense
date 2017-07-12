package cz.rudypokorny.expense.service.impl;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.AccountCreator;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.service.IExpenseService;
import cz.rudypokorny.expense.entity.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void findExpenseByFilter() throws Exception {
        Result<Iterable<Expense>> result = expenseService.findExpenseByFilter(null);

        assertTrue(result.isCompromised());
    }
}