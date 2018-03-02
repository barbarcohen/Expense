package cz.rudypokorny.expense.importexport.importing.mappers;

import cz.rudypokorny.expense.importexport.RecordMapper;
import cz.rudypokorny.expense.importexport.domain.CategoryMapping;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
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
import java.util.Objects;

/**
 * Created by Rudolf on 28/02/2018.
 */
//"02/08/2015","Rudík","Investments","Life Insurance","Aegon","Account Transfer","CZK","-1 150","","10BC8A31-3834-4286-9E5F-B0D0D124E265"
public class RudikCSVRecordMapper implements RecordMapper<CSVRecord, Expense, CSVFormat> {
    private static final String FILENAME = "source_rudy.csv";
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Logger logger = LoggerFactory.getLogger(RudikCSVRecordMapper.class);
    private final ZoneId timezone;
    private final Account ACCOUNT = Account.named("Rudík");

    public RudikCSVRecordMapper() {
        timezone = DateUtil.getCurrentTimeZone();
    }

    public Expense map(final CSVRecord csvRecord) {
        Objects.requireNonNull(csvRecord, "csvRecord cannot be null");
        Expense expense = null;
        try {
            Double amount = Double.valueOf(cleanAmountValue(csvRecord.get(7)));
            String subcategory = csvRecord.get(3);
            String category = csvRecord.get(2);
            String note = StringUtils.trimToNull(csvRecord.get(8));
            ZonedDateTime date = LocalDate.parse(csvRecord.get(0), DATETIME_FORMAT).atStartOfDay(timezone);
            String vendor = csvRecord.get(4);
            String currency = csvRecord.get(6);
            String extId = csvRecord.get(9);
            String payment = csvRecord.get(5);

            Category convertedCategory = CategoryMapping.getMappingFor(category, subcategory).full();
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

}
