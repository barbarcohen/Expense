package cz.rudypokorny.expense.tools.importexport.exporting.mappers;

import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.RecordMapper;
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
                .withHeader("Date", "Type", "Category", "Vendor", "Payment", "Currency", "Amount", "Note", "ID");
    }

    @Override
    public List<?> map(Expense expense) {
        return Arrays.asList(convertDate(expense.getWhen()), expense.getAccount().getName(), expense.getCategory().getName(),
                expense.getVendor(), expense.getPayment(), expense.getCurrency(), expense.getAmount(),
                expense.getNote(), expense.getExtId());
    }

    @Override
    public String getFileName() {
        return filename;
    }

    private String convertDate(ZonedDateTime when) {
        return DATE_FORMATTER.format(when);
    }

}
