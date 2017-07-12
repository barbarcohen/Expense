package cz.rudypokorny.expense.validator;

import cz.rudypokorny.expense.entity.Rules;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Rudolf on 11/07/2017.
 */
public class RulesTest {

    @Test
    public void areBrokenEmpty(){
        Rules rules = new Rules();
        assertFalse(rules.areBroken());
    }

    @Test
    public void areBrokenHasSome(){
        Rules rules = new Rules();
        rules.broken("some broken");
        assertTrue(rules.areBroken());
    }

}