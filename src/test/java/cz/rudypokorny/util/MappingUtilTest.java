package cz.rudypokorny.util;

import com.google.common.collect.ImmutableMap;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryMapping;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rudolf on 02/08/2017.
 */
public class MappingUtilTest {

    final private static Map<String, CategoryEnum> CATEGORY_MAPPING = ImmutableMap.<String, CategoryEnum>builder()
            .put("Household -> Groceries", CategoryEnum.FOOD_GROCERIES)
            .put("Sámoška-Potraviny a nápoje", CategoryEnum.FOOD_GROCERIES)
            .build();

    @Test
    public void getMappingForNotDefined() throws Exception {
        CategoryEnum result = CategoryMapping.doTheMapping(CATEGORY_MAPPING, "dummy");
        assertEquals(CategoryEnum.OTHERS, result);
    }

    @Test
    public void getMappingForEmptyString() throws Exception {
        CategoryEnum result = CategoryMapping.doTheMapping(CATEGORY_MAPPING, "");
        assertEquals(CategoryEnum.OTHERS, result);
    }

    @Test
    public void getMappingForNull() throws Exception {
        CategoryEnum result = CategoryMapping.doTheMapping(CATEGORY_MAPPING, null);
        assertEquals(CategoryEnum.OTHERS, result);
    }

    @Test
    public void getMappingExisting() throws Exception {
        CategoryEnum result = CategoryMapping.doTheMapping(CATEGORY_MAPPING, "Household -> Groceries");
        assertEquals(CategoryEnum.FOOD_GROCERIES, result);

        result = CategoryMapping.doTheMapping(CATEGORY_MAPPING, "Sámoška-Potraviny a nápoje");
        assertEquals(CategoryEnum.FOOD_GROCERIES, result);
    }

}