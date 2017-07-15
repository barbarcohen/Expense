package cz.rudypokorny.expense.entity;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Rudolf on 14/07/2017.
 */
public class ResultTest {

    @Test
    public void testWithObject() throws Exception {
        Result<Object> result = Result.create(new Object());
        assertTrue(result.isOk());
        assertFalse(result.isCompromised());
    }

    @Test
    public void testWithEmptyRules() throws Exception {
        Result<Object> result = Result.fail(new Rules());
        assertTrue(result.isOk());
        assertFalse(result.isCompromised());
    }

    @Test
    public void testWithErrorRules() throws Exception {
        Result<Object> result = Result.fail(new Rules().broken("message"));
        assertFalse(result.isOk());
        assertTrue(result.isCompromised());
    }

    @Test
    public void testWithExceptionRules() throws Exception {
        Result<Object> result = Result.fail(new Rules().exception(new NullPointerException()));
        assertFalse(result.isOk());
        assertTrue(result.isCompromised());
    }

    @Test
    public void testWithErrorAndExceptionRules() throws Exception {
        Result<Object> result = Result.fail(new Rules().broken("message").exception(new NullPointerException()));
        assertFalse(result.isOk());
        assertTrue(result.isCompromised());
    }

}