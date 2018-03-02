package cz.rudypokorny.expense.tools.importexport.importing.mappers;

import cz.rudypokorny.expense.model.Record;
import cz.rudypokorny.expense.tools.importexport.RecordMapper;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryMapping;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
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
public class RudikCSVRecordMapper implements RecordMapper<CSVRecord, Record, CSVFormat> {
    private static final String FILENAME = "source_rudik.csv";
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Logger logger = LoggerFactory.getLogger(RudikCSVRecordMapper.class);
    private final ZoneId timezone;
    private final Account ACCOUNT = Account.named("Rudík");

    public RudikCSVRecordMapper() {
        timezone = DateUtil.getCurrentTimeZone();
    }

    public Record map(final CSVRecord record) {
        Objects.requireNonNull(record, "csvRecord cannot be null");
        Record expense = null;
        try {
            Double amount = Double.valueOf(cleanAmountValue(record.get(7)));
            String subcategory = record.get(3);
            String category = record.get(2);
            String note = StringUtils.trimToNull(record.get(8));
            ZonedDateTime date = LocalDate.parse(record.get(0), DATETIME_FORMAT).atStartOfDay(timezone);
            String vendor = record.get(4);
            String currency = record.get(6);
            String extId = record.get(9);
            String payment = record.get(5);

            Category convertedCategory = CategoryMapping.getMappingFor(category, subcategory).full();
            expense = Record.newExpense(amount).
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
