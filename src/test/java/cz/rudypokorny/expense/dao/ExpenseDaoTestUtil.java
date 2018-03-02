package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Record;
import cz.rudypokorny.expense.model.User;
import cz.rudypokorny.util.SecurityContextTestUtil;
import org.assertj.core.util.IterableUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@javax.transaction.Transactional
public class ExpenseDaoTestUtil {


    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ExpenseDao expenseDao;

    private Account expectedAccount;
    private User currentUser;

    @Before
    public void setup() {
        expectedAccount = Account.named("testaccount");
        currentUser = SecurityContextTestUtil.addToSecurityContext(User.create("testuser", "password")).account(expectedAccount);

        testEntityManager.persist(currentUser);
        expectedAccount.activeFor(currentUser);
        testEntityManager.persist(expectedAccount);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    public void testFindAll() {
        Record expectedRecord = Record.newExpense(1d).by(expectedAccount);

        testEntityManager.persist(expectedRecord);

        Iterable<Record> result = expenseDao.findAll();

        assertFalse(IterableUtil.nonNullElementsIn(result).isEmpty());

        Record actualRecord = result.iterator().next();
        assertEquals(expectedRecord.getAccount(), actualRecord.getAccount());
        assertEquals(expectedRecord.getAmount(), actualRecord.getAmount());
        assertNotNull(actualRecord.getCreatedDate());
        assertNotNull(actualRecord.getUpdatedDate());
    }

    @Test
    public void testFindByWhenBetweenNanoInPast(){

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expectedTime = now.minusNanos(1);

        Record recordInPast = Record.newExpense(1.0).by(expectedAccount).at(expectedTime);

        testEntityManager.persistAndFlush(recordInPast);

        //from now to future + nano
        assertFalse(expenseDao.findByWhenBetween(now, now.plusNanos(1)).iterator().hasNext());
        //from past 2 days to now - 2 manos
        assertFalse(expenseDao.findByWhenBetween(now.minusDays(2), now.minusNanos(2)).iterator().hasNext());

        //in past from now
        Record actualRecord = expenseDao.findByWhenBetween(now.minusDays(1), now).iterator().next();
        assertEquals(recordInPast.getId(), actualRecord.getId());
        assertEquals(recordInPast.getAmount(), actualRecord.getAmount());

    }
}
