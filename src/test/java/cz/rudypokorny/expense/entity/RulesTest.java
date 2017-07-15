package cz.rudypokorny.expense.entity;

import org.junit.Test;

import static org.junit.Assert.*;


public class RulesTest {

    @Test
    public void areBrokenEmtpy() throws Exception {
        Rules rules = new Rules();

        assertFalse(rules.areBroken());
        assertTrue(rules.getExceptions().isEmpty());
        assertTrue(rules.getErrors().isEmpty());
    }

    @Test
    public void areBrokenWithError() throws Exception {
        String expectedMessage = "sdfsdg sdg dsg sd g";
        Rules rules = new Rules().broken(expectedMessage);

        assertTrue(rules.areBroken());
        assertTrue(rules.getExceptions().isEmpty());
        assertEquals(1, rules.getErrors().size());
        assertEquals(expectedMessage, rules.getErrors().get(0));
    }

    @Test
    public void areBrokenWithException() throws Exception {
        NullPointerException expectedException = new NullPointerException();
        Rules rules = new Rules().exception(expectedException);

        assertTrue(rules.areBroken());
        assertTrue(rules.getErrors().isEmpty());
        assertEquals(1, rules.getExceptions().size());
        assertEquals(expectedException, rules.getExceptions().get(0));
    }

    @Test
    public void areBrokenWithErrorAndException() throws Exception {
        String expectedMessage = "sdfsdg sdg dsg sd g";
        NullPointerException expectedException = new NullPointerException();
        Rules rules = new Rules().broken(expectedMessage).exception(expectedException);

        assertTrue(rules.areBroken());
        assertEquals(1, rules.getExceptions().size());
        assertEquals(1, rules.getErrors().size());
        assertEquals(expectedMessage, rules.getErrors().get(0));
        assertEquals(expectedMessage, rules.getErrors().get(0));
    }
}