package cz.rudypokorny.expense.tools.importexport.importing.mappers;

import com.google.common.collect.ImmutableMap;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.RecordMapper;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryMapping;
import cz.rudypokorny.util.DateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

import static cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum.*;

/**
 * Created by Rudolf on 28/02/2018.
 */
//"02/08/2015","Rudík","Investments","Life Insurance","Aegon","Account Transfer","CZK","-1 150","","10BC8A31-3834-4286-9E5F-B0D0D124E265"
public class RudikCSVRecordMapper implements RecordMapper<CSVRecord, Expense, CSVFormat> {
    private static final String FILENAME = "source_rudik.csv";
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Logger logger = LoggerFactory.getLogger(RudikCSVRecordMapper.class);
    //mapping rudy
    final private static Map<String, CategoryEnum> RUDIK_MAPPING = ImmutableMap.<String, CategoryEnum>builder().
            put("Investments -> Life Insurance", FINANCIAL_INSURANCES).
            put("Investments -> Investments", INVESTMENTS_FINANCIAL).
            put("Investments -> Pension Fund", INVESTMENTS_PENSION).

            put("Home -> Home Improvement", HOUSING_FURNITURE).
            put("Home -> Maintenance and Repair", HOUSING_REPAIRS).
            put("Home -> Mortgage", HOUSING_MORTGAGE).
            put("Home -> Loan", HOUSING_MORTGAGE).
            put("Household -> Groceries", FOOD_GROCERIES).
            put("Home -> Services", HOUSING_ENERGY).
            put("Home -> Insurance", HOUSING_INSURANCE).
            put("Home -> Internet", INTERNET).
            put("Utilities -> internet", INTERNET).
            put("Household -> Supplies", HOUSING_GENERAL).
            put("Home -> Electricity", HOUSING_ENERGY).
            put("Home -> Garden", SHOPPING_HOME_GARDEN).

            put("Entertainment -> Shows", LIFE_CULTURE).
            put("Entertainment -> Booz", LIFE_PARTY).
            put("Entertainment -> Movies", LIFE_CULTURE).
            put("Entertainment -> Apps", SOFTWARE).
            put("Entertainment -> Other", LIFE_CULTURE).
            put("Entertainment -> Games", SOFTWARE).
            put("Entertainment -> Party", LIFE_PARTY).
            put("Entertainment -> Music", LIFE_BOOKS_AUDIO).
            put("Entertainment -> Books", LIFE_BOOKS_AUDIO).
            put("Entertainment -> Dance", LIFE_CULTURE).
            put("Utilities -> Apps - Others", SOFTWARE).

            put("Food -> Restaurant", FOOD_RESTAURANT).
            put("Food -> Lunch", FOOD_WORK_LUNCH).
            put("Food -> Snack", FOOD_SNACKS).
            put("Food -> Dinner", FOOD_RESTAURANT).
            put("Food -> Breakfast", FOOD_SNACKS).

            put("Utilities -> CellPhone", PHONE).

            put("Income -> Rent MB", INCOME_RENTAL).
            put("Income -> Bonus", INCOME_WAGE).
            put("Income -> Meal Tickets", INCOME_COUPONS).
            put("Income -> Salary", INCOME_WAGE).
            put("Income -> Photo", INCOME_OTHER).

            put("Personal -> Self Improvement", LIFE_EDUCATION).
            put("Personal -> Haircuts", LIFE_WELLNESS_BEAUTY).
            put("Personal -> Clothing", SHOPPING_CLOTHES).
            put("Personal -> Medical", LIFE_HEALCARE).
            put("Personal -> Massage", LIFE_WELLNESS_BEAUTY).
            put("Personal -> Gift", SHOPPING_GIFTS).
            put("Personal -> Dental", LIFE_HEALCARE).
            put("Personal -> Other", SHOPPING_FREE_TIME).
            put("Personal -> Baby", SHOPPING_BABY).

            put("Sport -> Fees", LIFE_SPORT).
            put("Sport -> Equipment", LIFE_SPORT).

            put("Transportation -> MHD", TRANSPORTATION_MHD).
            put("Transportation -> Train", TRANSPORTATION).
            put("Transportation -> Taxi/Cab", TRANSPORTATION).
            put("Transportation -> Bus", TRANSPORTATION).

            put("Electronics -> Camera", SHOPPING_CAMERA_ACCESSORIES).
            put("Electronics -> Phone", SHOPPING_PHONE_ACCESSORIES).
            put("Electronics -> Computer", PC).
            put("Electronics -> Audio", SHOPPING_ELECTRONICS).
            put("Electronics -> Other", SHOPPING_ELECTRONICS).
            put("Electronics -> Game Consoles", SHOPPING_ELECTRONICS).

            put("Auto -> Repair", VEHICLE_MAINTENANCE).
            put("Auto -> Insurance", VEHICLE_INSURANCE).
            put("Auto -> Gas", VEHICLE_FUEL).
            put("Auto -> Other", FINANCIAL_FINES).
            put("Auto -> Loan", VEHICLE_LOAN).
            put("Auto -> Gasoline", VEHICLE_FUEL).
            put("Auto -> Stuff", VEHICLE_GENERAL).

            put("Vacation -> Hotels", LIFE_HOLIDAYS).
            put("Vacation -> Sightseeings", LIFE_HOLIDAYS).
            put("Vacation -> ", LIFE_HOLIDAYS).
            put("Vacation -> Transport", LIFE_HOLIDAYS).
            put("Vacation -> Air Tickets", LIFE_HOLIDAYS).
            put("Vacation -> Insurance", FINANCIAL_INSURANCES).
            put("Vacation -> Food", FOOD_SNACKS).
            put("Vacation -> Other", LIFE_HOLIDAYS).
            build();
    public static final String DEFAULT_VENDOR = "None";
    public static final String EMPTY_STRING = "";
    private final ZoneId timezone;
    private final Account ACCOUNT = Account.named("Rudík");

    public RudikCSVRecordMapper() {
        timezone = DateUtil.getCurrentTimeZone();
    }

    public Expense map(final CSVRecord record) {
        Objects.requireNonNull(record, "csvRecord cannot be null");
        Expense expense = null;
        try {
            Double amount = Double.valueOf(cleanAmountValue(record.get(7)));
            String subcategory = record.get(3);
            String category = record.get(2);
            String note = StringUtils.trimToNull(record.get(8));
            ZonedDateTime date = LocalDate.parse(record.get(0), DATETIME_FORMAT).atStartOfDay(timezone);
            String vendor = convertVendor(record.get(4));
            String currency = record.get(6);
            String extId = record.get(9);
            String payment = record.get(5);

            CategoryEnum convertedCategory = CategoryMapping.doTheMapping(RUDIK_MAPPING, category + " -> " + subcategory);
            expense = Expense.newExpense(amount).
                    on(convertedCategory).
                    by(ACCOUNT).
                    at(date).
                    vendor(vendor).
                    currency(currency).
                    payment(payment).
                    EXT(extId).
                    noted(note);

            logger.trace("Converted entity: " + expense.toString());

        } catch (Exception e) {
            logger.error("Importing failed for Reason: {}", e);
        }

        return expense;
    }

    @Override
    public String getFileName() {
        return FILENAME;
    }

    @Override
    public CSVFormat getConfig() {
        return CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader();
    }


    private String cleanAmountValue(String value) {
        return value.replaceAll(",", ".").replaceAll("[^\\d.-]", "");
    }

    private String convertVendor (String vendor){
        return DEFAULT_VENDOR.equals(vendor) ? EMPTY_STRING : vendor;
    }
}
