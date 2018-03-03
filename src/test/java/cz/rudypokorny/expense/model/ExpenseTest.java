package cz.rudypokorny.expense.model;

import cz.rudypokorny.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateUtil.class)
public class ExpenseTest {

    @Test(expected = NullPointerException.class)
    public void newExpenseWithNull() {
        Expense result = Expense.newExpense(null);
    }

    @Test
    public void newExpense() {
        ZonedDateTime expectedDateTIme = ZonedDateTime.now();
        PowerMockito.mockStatic(DateUtil.class);
        Mockito.when(DateUtil.getCurrentDateTime()).thenReturn(expectedDateTIme);
        double expected = 1.23;
        Expense result = Expense.newExpense(new Double(expected));

        assertEquals(Double.valueOf(expected), result.getAmount());
        assertEquals(expectedDateTIme, result.getWhen());
    }

    @Test
    public void testWithNote() throws Exception {
        String expected = "Some value";
        Expense result = Expense.newExpense(1d);

        assertNull(result.getNote());
        assertEquals(expected, result.noted(expected).getNote());
    }

    @Test
    public void forAccount() throws Exception {
        Account expected = new Account();
        Expense result = Expense.newExpense(1d);

        assertNull(result.getAccount());
        assertEquals(expected, result.by(expected).getAccount());
    }

}