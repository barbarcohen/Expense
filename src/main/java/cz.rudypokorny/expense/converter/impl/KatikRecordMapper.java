package cz.rudypokorny.expense.converter.impl;

import cz.rudypokorny.expense.converter.RecordMapper;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.util.DateUtil;
import cz.rudypokorny.expense.converter.CategoryMapping;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * //7/26/17;Miminko;477;;Vana,prebalovak +povlak
 */
public class KatikRecordMapper implements RecordMapper<Expense> {

    private static final String FILENAME = "source_kata.csv";
    private static final String DATEFORMAT = "MM/dd/yy";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATEFORMAT);
    private static final String CURRENCY = "CZK";
    private static final Logger logger = LoggerFactory.getLogger(KatikRecordMapper.class);

    private final ZoneId timezone;

    public KatikRecordMapper() {
        timezone = DateUtil.getCurrentTimeZone();
    }

    public Expense map(final CSVRecord csvRecord) {
        Objects.requireNonNull(csvRecord, "csvRecord cannot be null");
        Expense expense = null;
        try {
            Double amount = Double.valueOf(cleanAmountValue(csvRecord.get(2)));
            String category = csvRecord.get(1);
            String note = StringUtils.trimToNull(csvRecord.get(4));

            ZonedDateTime date =DATE_FORMAT.parse(csvRecord.get(0)).toInstant().atZone(timezone);

            Category convertedCategory = CategoryMapping.getMappingFor(category).full();
            expense = Expense.newExpense(amount).
                    on(convertedCategory).
                    at(date).
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

    private String cleanAmountValue(String value) {
        return value.replaceAll("[^\\d.-]", "");
    }
}
