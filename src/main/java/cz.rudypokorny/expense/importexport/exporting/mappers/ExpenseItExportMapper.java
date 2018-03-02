package cz.rudypokorny.expense.importexport.exporting.mappers;

import cz.rudypokorny.expense.importexport.RecordMapper;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.util.DateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ExpenseItExportMapper implements RecordMapper<Expense, List<?>, CSVFormat> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final String filename;

    /**
     * Record format:
     * "02/08/2015","Rudík","Investments","Life Insurance","Aegon","Account Transfer","CZK","-1 150","","10BC8A31-3834-4286-9E5F-B0D0D124E265"
     *
     * @param filename
     */
    public ExpenseItExportMapper(String filename) {
        this.filename = filename + "_" + DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm").format(DateUtil.getCurrentDateTime()) + ".csv";
    }

    @Override
    public CSVFormat getConfig() {
        return CSVFormat.DEFAULT.withDelimiter(',').withQuoteMode(QuoteMode.ALL).withQuote('"')
                .withHeader("Date", "Type", "Category", "Subcategory", "Vendor", "Payment", "Currency", "Amount", "Note", "ID");
    }

    @Override
    public List<?> map(Expense record) {
        //TODO fix category.geChilredn
        return Arrays.asList(convertDate(record.getWhen()), record.getAccount().getName(), record.getCategory().getParent().getName(), record.getCategory().getName(),
                record.getVendor(), record.getPayment(), record.getCurrency(), record.getAmount(), record.getNote(), record.getExtId());
    }

    @Override
    public String getFileName() {
        return filename;
    }

    private String convertDate(ZonedDateTime when) {
        return DATE_FORMATTER.format(when);
    }

}
