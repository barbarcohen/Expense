package cz.rudypokorny.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rudolf on 02/08/2017.
 */
public class MappingUtilTest {


    @Test
    public void getMappingForNotDefined() throws Exception {
        CategoryEnum result = MappingUtil.getMappingFor("dummy");
        assertEquals(CategoryEnum.NOT_DEFINED, result);
    }

    @Test
    public void getMappingForEmptyString() throws Exception {
        CategoryEnum result = MappingUtil.getMappingFor("");
        assertEquals(CategoryEnum.NOT_DEFINED, result);
    }

    @Test
    public void getMappingForNull() throws Exception {
        CategoryEnum result = MappingUtil.getMappingFor(null);
        assertEquals(CategoryEnum.NOT_DEFINED, result);
    }

    @Test
    public void getMappingExisting() throws Exception {
        CategoryEnum result = MappingUtil.getMappingFor("Household -> Groceries");
        assertEquals(CategoryEnum.DOMACNOST_POTRAVINY, result);

        result = MappingUtil.getMappingFor("Sámoška-Potraviny a nápoje");
        assertEquals(CategoryEnum.DOMACNOST_POTRAVINY, result);
    }

}