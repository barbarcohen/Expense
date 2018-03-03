package cz.rudypokorny.expense.tools.importexport.domain;

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
            put("Investments -> Life Insurance", CategoryEnum.INVESTMENTS_INSURANCE).
            put("Investments -> Investments", CategoryEnum.INVESTMENTS_INVESTMENTS).
            put("Investments -> Pension Fund", CategoryEnum.INVESTMENTS_PENSION).

            put("Home -> Home Improvement", CategoryEnum.HOUSEHOLD_IMPROVEMENT).
            put("Home -> Maintenance and Repair", CategoryEnum.HOUSEHOLD_MAINTENANCE_REPAIRS).
            put("Home -> Mortgage", CategoryEnum.HOUSEHOLD_MORTGAGE).
            put("Home -> Loan", CategoryEnum.HOUSEHOLD_MORTGAGE).
            put("Household -> Groceries", CategoryEnum.HOUSEHOLD_GROCERIES).
            put("Home -> Services", CategoryEnum.HOUSEHOLD_SERVICES_FEES).
            put("Home -> Insurance", CategoryEnum.HOUSEHOLD_INSURANCE).
            put("Home -> Internet", CategoryEnum.HOUSEHOLD_SERVICES_FEES).
            put("Utilities -> internet", CategoryEnum.HOUSEHOLD_SERVICES_FEES).
            put("Household -> Supplies", CategoryEnum.HOUSEHOLD_OTHER).
            put("Home -> Electricity", CategoryEnum.HOUSEHOLD_ELECTRICITY).
            put("Home -> Garden", CategoryEnum.HOUSEHOLD_GARDEN).

            put("Entertainment -> Shows", CategoryEnum.ENTERTAINMENT_CULTURE).
            put("Entertainment -> Booz", CategoryEnum.ENTERTAINMENT_PARTY_PUB).
            put("Entertainment -> Movies", CategoryEnum.ENTERTAINMENT_CULTURE).
            put("Entertainment -> Apps", CategoryEnum.ENTERTAINMENT_APPS_GAMES).
            put("Entertainment -> Other", CategoryEnum.ENTERTAINMENT_OTHER).
            put("Entertainment -> Games", CategoryEnum.ENTERTAINMENT_APPS_GAMES).
            put("Entertainment -> Party", CategoryEnum.ENTERTAINMENT_PARTY_PUB).
            put("Entertainment -> Music", CategoryEnum.ENTERTAINMENT_APPS_GAMES).
            put("Entertainment -> Books", CategoryEnum.ENTERTAINMENT_BOOKS).
            put("Entertainment -> Dance", CategoryEnum.ENTERTAINMENT_OTHER).
            put("Utilities -> Apps - Others", CategoryEnum.ENTERTAINMENT_APPS_GAMES).

            put("Food -> Restaurant", CategoryEnum.FOOD_RESTAURANT).
            put("Food -> Lunch", CategoryEnum.FOOD_WORK_LUNCH).
            put("Food -> Snack", CategoryEnum.FOOD_SNACK).
            put("Food -> Dinner", CategoryEnum.FOOD_RESTAURANT).
            put("Food -> Breakfast", CategoryEnum.FOOD_SNACK).

            put("Utilities -> CellPhone", CategoryEnum.PERSONAL_FEES).//TODO add note

            put("Income -> Rent MB", CategoryEnum.INCOME_OTHER).
            put("Income -> Bonus", CategoryEnum.INCOME_SALARY).
            put("Income -> Meal Tickets", CategoryEnum.INCOME_SALARY).
            put("Income -> Salary", CategoryEnum.INCOME_SALARY).
            put("Income -> Photo", CategoryEnum.INCOME_OTHER).

            put("Personal -> Self Improvement", CategoryEnum.PERSONAL_EDUCATION).
            put("Personal -> Haircuts", CategoryEnum.PERSONAL_OTHER).
            put("Personal -> Clothing", CategoryEnum.FASHION_CLOTHES).
            put("Personal -> Medical", CategoryEnum.HOUSEHOLD_MEDICINE).
            put("Personal -> Massage", CategoryEnum.WELLNESS_WELLNESS).
            put("Personal -> Gift", CategoryEnum.PERSONAL_GIFTS).
            put("Personal -> Dental", CategoryEnum.HOUSEHOLD_HEALTHCARE).
            put("Personal -> Other", CategoryEnum.PERSONAL_OTHER).
            put("Personal -> Baby", CategoryEnum.BABY_OTHER).

            put("Sport -> Fees", CategoryEnum.SPORT_FEES).
            put("Sport -> Equipment", CategoryEnum.SPORT_EQUIPMENT).

            put("Transportation -> MHD", CategoryEnum.PERSONAL_TRANSPORTATION).
            put("Transportation -> Train", CategoryEnum.PERSONAL_TRANSPORTATION).
            put("Transportation -> Taxi/Cab", CategoryEnum.PERSONAL_TRANSPORTATION).
            put("Transportation -> Bus", CategoryEnum.PERSONAL_TRANSPORTATION).

            put("Electronics -> Camera", CategoryEnum.ELECTRONICS_CAMERA).
            put("Electronics -> Phone", CategoryEnum.ELECTRONICS_PHONE).
            put("Electronics -> Computer", CategoryEnum.ELECTRONICS_COMPUTER).
            put("Electronics -> Audio", CategoryEnum.ELECTRONICS_COMPUTER).
            put("Electronics -> Other", CategoryEnum.ELECTRONICS_OTHER).
            put("Electronics -> Game Consoles", CategoryEnum.ELECTRONICS_OTHER).

            put("Auto -> Repair", CategoryEnum.CAR_REPAIRS_MAINTENANCE).
            put("Auto -> Insurance", CategoryEnum.CAR_INSURANCE).
            put("Auto -> Gas", CategoryEnum.CAR_FUEL).
            put("Auto -> Other", CategoryEnum.CAR_FEES).
            put("Auto -> Loan", CategoryEnum.CAR_LOAN).
            put("Auto -> Gasoline", CategoryEnum.CAR_FUEL).
            put("Auto -> Stuff", CategoryEnum.CAR_OTHER).

            put("Vacation -> Hotels", CategoryEnum.VACATION_ACCOMMODATION).
            put("Vacation -> Sightseeings", CategoryEnum.VACATION_FEES).
            put("Vacation -> ", CategoryEnum.VACATION_OTHER).
            put("Vacation -> Transport", CategoryEnum.VACATION_TRANSPORTATION).
            put("Vacation -> Air Tickets", CategoryEnum.VACATION_TRANSPORTATION).
            put("Vacation -> Insurance", CategoryEnum.VACATION_FEES).
            put("Vacation -> Food", CategoryEnum.VACATION_FOOD).
            put("Vacation -> Other", CategoryEnum.VACATION_OTHER).
            build();

    //mapping katka
    final private static Map<String, CategoryEnum> otherMapping = ImmutableMap.<String, CategoryEnum>builder().
            put("Volný čas-kino,koncert..", CategoryEnum.ENTERTAINMENT_CULTURE).
            put("K+R", CategoryEnum.HOUSEHOLD_GROCERIES).
            put("drogerie", CategoryEnum.HOUSEHOLD_SUPPLIES).
            put("lékárna", CategoryEnum.HOUSEHOLD_HEALTHCARE).//jenom kata
            put("Kavárna", CategoryEnum.FOOD_SNACK).
            put("jidlo", CategoryEnum.FOOD_SNACK).
            put("Doprava", CategoryEnum.CAR_FUEL).//auto (1000+) + all others MHD
            put("Na jedno", CategoryEnum.ENTERTAINMENT_PARTY_PUB).
            put("Miminko", CategoryEnum.BABY_OTHER).//leky vybaneni jidlo plinky
            put("společná domácnost", CategoryEnum.HOUSEHOLD_IMPROVEMENT).//? jidlo nebo vybaveni?
            put("Oblečení boty", CategoryEnum.FASHION_CLOTHES).//boty taky
            put("Ostatní", CategoryEnum.PERSONAL_OTHER).//viz poznamky
            put("Sport", CategoryEnum.SPORT_FEES).// fees & vybaveni - viz poznamky
            put("Mlsání", CategoryEnum.FOOD_SWEETS).
            put("Nutné výdaje", CategoryEnum.INVESTMENTS_INVESTMENTS).//pojisteni a investice
            put("Zvaní na pokrm,pití", CategoryEnum.FOOD_RESTAURANT).
            put("dovolená", CategoryEnum.VACATION_FEES).
            put("obědy", CategoryEnum.FOOD_WORK_LUNCH).
            put("dárky k narozeninám", CategoryEnum.PERSONAL_GIFTS).
            put("Zdraví,relax,sauna,lymf.", CategoryEnum.WELLNESS_WELLNESS).
            put("Restaurace", CategoryEnum.FOOD_RESTAURANT).
            put("doktor", CategoryEnum.HOUSEHOLD_MEDICINE).
            put("dárky vánoční", CategoryEnum.PERSONAL_GIFTS_CHRISTMAS).
            put("Polsko nákupy společné", CategoryEnum.VACATION_OTHER).
            put("Polsko nákupy mé", CategoryEnum.VACATION_OTHER).
            put("pokuty", CategoryEnum.CAR_FEES).
            put("Sámoška-Potraviny a nápoje", CategoryEnum.HOUSEHOLD_GROCERIES).
            put("Income-Salary", CategoryEnum.INCOME_SALARY).
            put("Income-Other", CategoryEnum.INCOME_OTHER).
            build();

    private static Logger logger = LoggerFactory.getLogger(CategoryMapping.class);

    //TODO add note at the note beggining => in case of moveing between domain
    public static CategoryEnum getMappingFor(final String category, String subCategory) {
        return getMappingFor(category + " -> " + subCategory);
    }
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
