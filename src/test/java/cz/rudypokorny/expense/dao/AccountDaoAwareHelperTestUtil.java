package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Account;

import static org.junit.Assert.*;

import cz.rudypokorny.expense.model.User;
import cz.rudypokorny.util.SecurityContextTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@javax.transaction.Transactional
public class AccountDaoAwareHelperTestUtil{


    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AccountDao accountDao;


    @Test(expected = DataIntegrityViolationException.class)
    public void testSaveDuplicitName(){
        String name = "name 123";
        Account account = Account.named(name);
        accountDao.save(account);

        accountDao.save(Account.named(name));
    }

    @Test
    public void testSave(){
        User user = SecurityContextTestUtil.addToSecurityContext(User.create("some","some"));
        testEntityManager.persistAndFlush(user);

        String name =   "name 123";
        Account expectedAccount = accountDao.save(Account.named(name));

        testEntityManager.flush();

        Account actualAccount = testEntityManager.find(Account.class, expectedAccount.getId());
        assertEquals(expectedAccount.getId(), actualAccount.getId());
        assertEquals(expectedAccount.getName(), actualAccount.getName());
        assertNotNull(actualAccount.getCreatedDate());
        assertNotNull(actualAccount.getUpdatedDate());
        assertTrue(actualAccount.getExpenses().isEmpty());
    }
}
