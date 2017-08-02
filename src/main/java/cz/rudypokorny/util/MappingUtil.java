package cz.rudypokorny.util;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static cz.rudypokorny.util.CategoryEnum.*;

/**
 * Created by Rudolf on 02/08/2017.
 */
public class MappingUtil {

    //mapping rudy
    final private static Map<String, CategoryEnum> expenseItMapping = ImmutableMap.<String, CategoryEnum>builder().
            put("Investments -> Life Insurance", TBD).
            put("Food -> Lunch", TBD).
            put("Sport -> Fees", TBD).
            put("Home -> Home Improvement", TBD).
            put("Household -> Groceries", DOMACNOST_POTRAVINY).
            put("Transportation -> MHD", TBD).
            put("Entertainment -> Shows", TBD).
            put("Entertainment -> Booz", TBD).
            put("Entertainment -> Movies", TBD).
            put("Food -> Snack", TBD).
            put("Utilities -> CellPhone", TBD).
            put("Auto -> Gas", TBD).
            put("Auto -> Other", TBD).
            put("Income -> Rent MB", TBD).
            put("Home -> Maintenance and Repair", TBD).
            put("Transportation -> Train", TBD).
            put("Income -> Bonus", TBD).
            put("Food -> Restaurant", TBD).
            put("Home -> Mortgage", TBD).
            put("Vacation -> Hotels", TBD).
            put("Home -> Electricity", TBD).
            put("Entertainment -> Apps", TBD).
            put("Investments -> Investments", TBD).
            put("Home -> Services", TBD).
            put("Entertainment -> Other", TBD).
            put("Personal -> Gift", TBD).
            put("Income -> Photo", TBD).
            put("Household -> Supplies", TBD).
            put("Entertainment -> Party", TBD).
            put("Personal -> Dental", TBD).
            put("Personal -> Other", TBD).
            put("Auto -> Repair", TBD).
            put("Food -> Breakfast", TBD).
            put("Auto -> Stuff", TBD).
            put("Income -> Meal Tickets", TBD).
            put("Auto -> Gasoline", TBD).
            put("Income -> Salary", TBD).
            put("Personal -> Self Improvement", TBD).
            put("Personal -> Haircuts", TBD).
            put("Entertainment -> Books", TBD).
            put("Vacation -> Other", TBD).
            put("Entertainment -> Games", TBD).
            put("Electronics -> Other", TBD).
            put("Utilities -> Apps - Others", TBD).
            put("Personal -> Clothing", TBD).
            put("Vacation -> Food", TBD).
            put("Home -> Insurance", TBD).
            put("Electronics -> Phone", TBD).
            put("Personal -> Medical", TBD).
            put("Sport -> Equipment", TBD).
            put("Electronics -> Audio", TBD).
            put("Transportation -> Taxi/Cab", TBD).
            put("Investments -> Pension Fund", TBD).
            put("Personal -> Massage", TBD).
            put("Entertainment -> Dance", TBD).
            put("Transportation -> Bus", TBD).
            put("Vacation -> Transport", TBD).
            put("Electronics -> Computer", TBD).
            put("Auto -> Insurance", TBD).
            put("Utilities -> internet", TBD).
            put("Vacation -> Air Tickets", TBD).
            put("Vacation -> Insurance", TBD).
            put("Vacation ->", TBD).
            put("Electronics -> Camera", TBD).
            put("Food -> Dinner", TBD).
            put("Auto -> Loan", TBD).
            put("Vacation -> Sightseeings", TBD).
            build();

    //mapping katka
    final private static Map<String, CategoryEnum> otherMapping = ImmutableMap.<String, CategoryEnum>builder().
            put("Volný čas-kino,koncert..", TBD).
            put("K+R", TBD).
            put("drogerie", TBD).
            put("lékárna", TBD).
            put("Kavárna", TBD).
            put("jidlo", TBD).
            put("Doprava", TBD).
            put("Na jedno", TBD).
            put("Miminko", TBD).
            put("společná domácnost", TBD).
            put("Oblečení boty", TBD).
            put("Ostatní", TBD).
            put("Sport", TBD).
            put("Mlsání", TBD).
            put("Nutné výdaje", TBD).
            put("Zvaní na pokrm,pití", TBD).
            put("dovolená", TBD).
            put("obědy", TBD).
            put("dárky k narozeninám", TBD).
            put("Zdraví,relax,sauna,lymf.", TBD).
            put("Restaurace", TBD).
            put("doktor", TBD).
            put("dárky vánoční", TBD).
            put("Polsko nákupy společné", TBD).
            put("Polsko nákupy mé", TBD).
            put("pokuty", TBD).
            put("Sámoška-Potraviny a nápoje", DOMACNOST_POTRAVINY).
            build();

    private static Logger logger = LoggerFactory.getLogger(MappingUtil.class);

    public static CategoryEnum getMappingFor(final String sourceCategoryName) {
        Optional<Map.Entry<String, CategoryEnum>> result = Stream.of(expenseItMapping, otherMapping).
                map(Map::entrySet).flatMap(Collection::stream).
                filter(e -> e.getKey().equals(sourceCategoryName)).findFirst();
        if (result.isPresent()) {
            return result.get().getValue();
        }
        logger.error("Mapping for category named '{}' was not found.", sourceCategoryName);
        return NOT_DEFINED;

    }


}
