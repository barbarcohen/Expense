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

import java.time.LocalDate;
import java.time.ZonedDateTime;

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

    @Test
    public void testFindByWhenBetweenNanoInPast(){
        Account account = Account.named("account");

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expectedTime = now.minusNanos(1);

        Expense expenseInPast = Expense.newExpense(1.0).by(account).at(expectedTime);

        testEntityManager.persist(account);
        testEntityManager.persistAndFlush(expenseInPast);

        //from now to future + nano
        assertFalse(expenseDao.findByWhenBetween(now, now.plusNanos(1)).iterator().hasNext());
        //from past 2 days to now - 2 manos
        assertFalse(expenseDao.findByWhenBetween(now.minusDays(2), now.minusNanos(2)).iterator().hasNext());

        //in past from now
        Expense actualExpense = expenseDao.findByWhenBetween(now.minusDays(1), now).iterator().next();
        assertEquals(expenseInPast.getId(), actualExpense.getId());
        assertEquals(expenseInPast.getAmount(), actualExpense.getAmount());

    }
}
