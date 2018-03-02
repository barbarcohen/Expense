package cz.rudypokorny.util;

import cz.rudypokorny.expense.importexport.domain.CategoryEnum;
import cz.rudypokorny.expense.importexport.domain.CategoryMapping;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rudolf on 02/08/2017.
 */
public class MappingUtilTest {


    @Test
    public void getMappingForNotDefined() throws Exception {
        CategoryEnum result = CategoryMapping.getMappingFor("dummy");
        assertEquals(CategoryEnum.NOT_DEFINED, result);
    }

    @Test
    public void getMappingForEmptyString() throws Exception {
        CategoryEnum result = CategoryMapping.getMappingFor("");
        assertEquals(CategoryEnum.NOT_DEFINED, result);
    }

    @Test
    public void getMappingForNull() throws Exception {
        CategoryEnum result = CategoryMapping.getMappingFor(null);
        assertEquals(CategoryEnum.NOT_DEFINED, result);
    }

    @Test
    public void getMappingExisting() throws Exception {
        CategoryEnum result = CategoryMapping.getMappingFor("Household -> Groceries");
        assertEquals(CategoryEnum.HOUSEHOLD_GROCERIES, result);

        result = CategoryMapping.getMappingFor("Sámoška-Potraviny a nápoje");
        assertEquals(CategoryEnum.HOUSEHOLD_GROCERIES, result);
    }

}