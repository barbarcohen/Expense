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

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Created by Rudolf on 28/02/2018.
 */
public class RudyRecordMapper implements RecordMapper<Expense> {
    private static final String FILENAME = "source_rudy.csv";
    private static final String DATEFORMAT = "dd/MM/yyyy";
    private static final Logger logger = LoggerFactory.getLogger(RudyRecordMapper.class);

    private final ZoneId timezone;

    public RudyRecordMapper() {
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
            ZonedDateTime date = LocalDate.parse(csvRecord.get(0), DateTimeFormatter.ofPattern(DATEFORMAT)).atStartOfDay(timezone);
            String vendor = csvRecord.get(4);
            String currency = csvRecord.get(6);

            Category convertedCategory = CategoryMapping.getMappingFor(createCategoruString(category, subcategory)).full();
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

    private String createCategoruString(String category, String subcategory) {
        return category + " -> " + subcategory;
    }

    private String cleanAmountValue(String value) {
        return value.replaceAll("[^\\d.-]", "");
    }
}
