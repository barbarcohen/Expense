package cz.rudypokorny.expense.converter;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Rudolf on 02/08/2017.
 */
public class CategoryMapping {

    //mapping rudy
    final private static Map<String, CategoryEnum> expenseItMapping = ImmutableMap.<String, CategoryEnum>builder().
            put("Investments -> Life Insurance", CategoryEnum.TBD).
            put("Food -> Lunch", CategoryEnum.TBD).
            put("Sport -> Fees", CategoryEnum.TBD).
            put("Home -> Home Improvement", CategoryEnum.TBD).
            put("Household -> Groceries", CategoryEnum.HOUSEHOLD_GLOCERIES).
            put("Transportation -> MHD", CategoryEnum.TBD).
            put("Entertainment -> Shows", CategoryEnum.TBD).
            put("Entertainment -> Booz", CategoryEnum.TBD).
            put("Entertainment -> Movies", CategoryEnum.TBD).
            put("Food -> Snack", CategoryEnum.TBD).
            put("Utilities -> CellPhone", CategoryEnum.TBD).
            put("Auto -> Gas", CategoryEnum.TBD).
            put("Auto -> Other", CategoryEnum.TBD).
            put("Income -> Rent MB", CategoryEnum.TBD).
            put("Home -> Maintenance and Repair", CategoryEnum.TBD).
            put("Transportation -> Train", CategoryEnum.TBD).
            put("Income -> Bonus", CategoryEnum.TBD).
            put("Food -> Restaurant", CategoryEnum.TBD).
            put("Home -> Mortgage", CategoryEnum.TBD).
            put("Vacation -> Hotels", CategoryEnum.TBD).
            put("Home -> Electricity", CategoryEnum.TBD).
            put("Entertainment -> Apps", CategoryEnum.TBD).
            put("Investments -> Investments", CategoryEnum.TBD).
            put("Home -> Services", CategoryEnum.TBD).
            put("Entertainment -> Other", CategoryEnum.TBD).
            put("Personal -> Gift", CategoryEnum.TBD).
            put("Income -> Photo", CategoryEnum.TBD).
            put("Household -> Supplies", CategoryEnum.TBD).
            put("Entertainment -> Party", CategoryEnum.TBD).
            put("Personal -> Dental", CategoryEnum.TBD).
            put("Personal -> Other", CategoryEnum.TBD).
            put("Auto -> Repair", CategoryEnum.TBD).
            put("Food -> Breakfast", CategoryEnum.TBD).
            put("Auto -> Stuff", CategoryEnum.TBD).
            put("Income -> Meal Tickets", CategoryEnum.TBD).
            put("Auto -> Gasoline", CategoryEnum.TBD).
            put("Income -> Salary", CategoryEnum.TBD).
            put("Personal -> Self Improvement", CategoryEnum.TBD).
            put("Personal -> Haircuts", CategoryEnum.TBD).
            put("Entertainment -> Books", CategoryEnum.TBD).
            put("Vacation -> Other", CategoryEnum.TBD).
            put("Entertainment -> Games", CategoryEnum.TBD).
            put("Electronics -> Other", CategoryEnum.TBD).
            put("Utilities -> Apps - Others", CategoryEnum.TBD).
            put("Personal -> Clothing", CategoryEnum.TBD).
            put("Vacation -> Food", CategoryEnum.TBD).
            put("Home -> Insurance", CategoryEnum.TBD).
            put("Electronics -> Phone", CategoryEnum.TBD).
            put("Personal -> Medical", CategoryEnum.TBD).
            put("Sport -> Equipment", CategoryEnum.TBD).
            put("Electronics -> Audio", CategoryEnum.TBD).
            put("Transportation -> Taxi/Cab", CategoryEnum.TBD).
            put("Investments -> Pension Fund", CategoryEnum.TBD).
            put("Personal -> Massage", CategoryEnum.TBD).
            put("Entertainment -> Dance", CategoryEnum.TBD).
            put("Transportation -> Bus", CategoryEnum.TBD).
            put("Vacation -> Transport", CategoryEnum.TBD).
            put("Electronics -> Computer", CategoryEnum.TBD).
            put("Auto -> Insurance", CategoryEnum.TBD).
            put("Utilities -> internet", CategoryEnum.TBD).
            put("Vacation -> Air Tickets", CategoryEnum.TBD).
            put("Vacation -> Insurance", CategoryEnum.TBD).
            put("Vacation ->", CategoryEnum.TBD).
            put("Electronics -> Camera", CategoryEnum.TBD).
            put("Food -> Dinner", CategoryEnum.TBD).
            put("Auto -> Loan", CategoryEnum.TBD).
            put("Vacation -> Sightseeings", CategoryEnum.TBD).
            put("Vacation -> ", CategoryEnum.TBD).
            build();

    //mapping katka
    final private static Map<String, CategoryEnum> otherMapping = ImmutableMap.<String, CategoryEnum>builder().
            put("Volný čas-kino,koncert..", CategoryEnum.TBD).
            put("K+R", CategoryEnum.TBD).
            put("drogerie", CategoryEnum.TBD).
            put("lékárna", CategoryEnum.TBD).
            put("Kavárna", CategoryEnum.TBD).
            put("jidlo", CategoryEnum.TBD).
            put("Doprava", CategoryEnum.TBD).
            put("Na jedno", CategoryEnum.TBD).
            put("Miminko", CategoryEnum.TBD).
            put("společná domácnost", CategoryEnum.TBD).
            put("Oblečení boty", CategoryEnum.TBD).
            put("Ostatní", CategoryEnum.TBD).
            put("Sport", CategoryEnum.TBD).
            put("Mlsání", CategoryEnum.TBD).
            put("Nutné výdaje", CategoryEnum.TBD).
            put("Zvaní na pokrm,pití", CategoryEnum.TBD).
            put("dovolená", CategoryEnum.TBD).
            put("obědy", CategoryEnum.TBD).
            put("dárky k narozeninám", CategoryEnum.TBD).
            put("Zdraví,relax,sauna,lymf.", CategoryEnum.TBD).
            put("Restaurace", CategoryEnum.TBD).
            put("doktor", CategoryEnum.TBD).
            put("dárky vánoční", CategoryEnum.TBD).
            put("Polsko nákupy společné", CategoryEnum.TBD).
            put("Polsko nákupy mé", CategoryEnum.TBD).
            put("pokuty", CategoryEnum.TBD).
            put("Sámoška-Potraviny a nápoje", CategoryEnum.HOUSEHOLD_GLOCERIES).
            build();

    private static Logger logger = LoggerFactory.getLogger(CategoryMapping.class);

    public static CategoryEnum getMappingFor(final String sourceCategoryName) {
        Optional<Map.Entry<String, CategoryEnum>> result = Stream.of(expenseItMapping, otherMapping).
                map(Map::entrySet).flatMap(Collection::stream).
                filter(e -> e.getKey().equals(sourceCategoryName)).findFirst();
        if (result.isPresent()) {
            return result.get().getValue();
        }
        logger.warn("Mapping for category '{}' was not found.", sourceCategoryName);
        return CategoryEnum.NOT_DEFINED;

    }


}
