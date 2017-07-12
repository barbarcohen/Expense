package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Expense;
import org.assertj.core.util.IterableUtil;
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
public class ExpenseDaoTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ExpenseDao expenseDao;

    @Test
    public void testFindAll() {
        Account account = Account.named("Sone name");
        testEntityManager.persist(account);

        Expense expectedExpense = Expense.newExpense(1d).by(account);

        testEntityManager.persist(expectedExpense);

        Iterable<Expense> result = expenseDao.findAll();

        assertFalse(IterableUtil.nonNullElementsIn(result).isEmpty());

        Expense actualExpense = result.iterator().next();
        assertEquals(expectedExpense.getAccount(), actualExpense.getAccount());
        assertEquals(expectedExpense.getAmount(), actualExpense.getAmount());
        assertNotNull(actualExpense.getCreatedDate());
        assertNotNull(actualExpense.getUpdatedDate());
    }
}
