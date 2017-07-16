package cz.rudypokorny.expense.service.impl;

import cz.rudypokorny.expense.dao.AccountDao;
import cz.rudypokorny.expense.dao.CategoryDao;
import cz.rudypokorny.expense.dao.ExpenseDao;
import cz.rudypokorny.expense.entity.Result;
import cz.rudypokorny.expense.entity.Rules;
import cz.rudypokorny.expense.model.*;
import cz.rudypokorny.expense.service.IExpenseService;
import cz.rudypokorny.expense.validator.*;
import cz.rudypokorny.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateUtil.class)
public class ExpenseServiceTest {

    @Captor
    private ArgumentCaptor<Expense> expenseCaptor;

    @InjectMocks
    private IExpenseService expenseService = new ExpenseService();

    @Mock
    private ExpenseDao expenseDao;

    @Mock
    private AccountDao accountDao;

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private ExpenseValidator expenseValidator;

    @Mock
    private CategoryValidator categoryValidator;

    private ZonedDateTime EXPECTED_DATE_NOW = ZonedDateTime.now();
    private Double EXPECTED_AMOUNT = 33.90;

    @Before
    public void setup() {
        PowerMockito.mockStatic(DateUtil.class);
        when(DateUtil.getCurrentDateTime()).thenReturn(EXPECTED_DATE_NOW);
    }

    @Test
    public void spendBrokingRules() {
        when(expenseValidator.validateNew(any())).thenReturn(new Rules().broken("someError"));

        Expense givenExpense = Expense.newExpense(EXPECTED_AMOUNT).by(AccountCreator.create());

        Result<Expense> result = expenseService.spend(givenExpense);

        assertTrue(result.isCompromised());
        verify(expenseDao, never()).save(eq(givenExpense));
    }
    /*
        @Test
        public void spendWithEmpty() {
            expenseService.spend(new Expense());
        }

        @Test
        public void spendWithNullAmount() {
            expenseService.spend(Expense.newExpense(null));
        }

        @Test
        public void spendAtNull() {
            Account account = AccountCreator.create();
            when(accountDao.findOne(eq(account.getId()))).thenReturn(account);

            expenseService.spend(Expense.newExpense(EXPECTED_AMOUNT).by(account).at(null));
            verify(expenseDao).save(expenseCaptor.capture());

            Expense result = expenseCaptor.getValue();

            assertEquals(EXPECTED_AMOUNT, result.getAmount());
            assertEquals(EXPECTED_DATE_NOW, result.getWhen());
        }


        @Test
        public void spendWithNullAccount() {
            expenseService.spend(Expense.newExpense(EXPECTED_AMOUNT).by(null));
        }

        @Test
        public void spendWithEmtyAccount() {
            expenseService.spend(Expense.newExpense(EXPECTED_AMOUNT).by(new Account()));
        }

        @Test(expected = DataIntegrityException.class)
        public void spendWithNotExistingAccount(){
            Account account = AccountCreator.create();
            when(accountDao.findOne(eq(account.getId()))).thenReturn(null);

            expenseService.spend(Expense.newExpense(EXPECTED_AMOUNT).by(account));
        }

        @Test
        public void spendWithNotExistingCategory(){
            Account expectedAccount = AccountCreator.create();
            when(accountDao.findOne(eq(expectedAccount.getId()))).thenReturn(expectedAccount);
            Category category = CategoryCreator.create();
            when(categoryDao.findOne(eq(category.getId()))).thenReturn(null);

            Expense result = expenseService.spend(Expense.newExpense(EXPECTED_AMOUNT).by(expectedAccount).on(category));
            assertEquals(EXPECTED_AMOUNT, result.getAmount());
            assertEquals(EXPECTED_DATE_NOW, result.getWhen());
            assertEquals(expectedAccount, result.getAccountDetails());
            assertNull(result.getCategory());
        }
    */
    @Test
    public void spendWithValidRules() {
        String expectedNote = "abrakadabra";
        ZonedDateTime expectedWhen = ZonedDateTime.now().minusDays(3);
        Category expectedCategory = CategoryCreator.create();
        Account expectedAccount = AccountCreator.create();
        when(accountDao.findOne(eq(expectedAccount.getId()))).thenReturn(expectedAccount);
        when(categoryDao.findOne(eq(expectedCategory.getId()))).thenReturn(expectedCategory);
        when(expenseValidator.validateNew(any())).thenReturn(new Rules());

        Result<Expense> result = expenseService.spend(Expense.newExpense(EXPECTED_AMOUNT).
                at(expectedWhen).
                noted(expectedNote).
                by(expectedAccount).
                on(expectedCategory));
        assertFalse(result.isCompromised());
        verify(expenseDao).save(expenseCaptor.capture());

        Expense actualExpense = expenseCaptor.getValue();

        assertEquals(EXPECTED_AMOUNT, actualExpense.getAmount());
        assertEquals(expectedWhen, actualExpense.getWhen());
        assertEquals(expectedAccount, actualExpense.getAccount());
        assertEquals(expectedCategory, actualExpense.getCategory());
        assertEquals(expectedNote, actualExpense.getNote());
    }

    @Test
    public void getExpense() throws Exception {
        when(expenseDao.findOne(any())).thenReturn(null);

        Result<Optional<Expense>> result = expenseService.getExpense(1L);
        assertFalse(result.get().isPresent());
        assertFalse(result.isCompromised());
    }

    @Test
    public void findExpenseByFilter() throws Exception {
     }

    @Test
    public void findAccounts() throws Exception {

    }

    @Test
    public void getAccount() throws Exception {

    }

    @Test
    public void getCategories() throws Exception {

    }

    @Test
    public void categorizeBreakingRules() {
        when(categoryValidator.validateNew(any())).thenReturn(new Rules().broken("some broken"));

        Result<Category> result = expenseService.categorize(null);
        assertTrue(result.isCompromised());
    }

    @Test
    public void categorize() {
        when(categoryValidator.validateNew(any())).thenReturn(new Rules());

        expenseService.categorize(new Category());
    }

}