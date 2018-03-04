package cz.rudypokorny.expense.tools.importexport.domain;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum.*;

/**
 * Created by Rudolf on 02/08/2017.
 */
public class CategoryMapping {

    private static Logger logger = LoggerFactory.getLogger(CategoryMapping.class);

    public static CategoryEnum doTheMapping(Map<String, CategoryEnum> map, final String sourceCategoryName) {
        Optional<Map.Entry<String, CategoryEnum>> result = Stream.of(map).
                map(Map::entrySet).flatMap(Collection::stream).
                filter(e -> e.getKey().equals(sourceCategoryName)).findFirst();
        if (result.isPresent()) {
            return result.get().getValue();
        }
        logger.warn("Mapping for category '{}' was not found.", sourceCategoryName);
        return OTHERS;
    }

}
