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

public class WalletSimpleExportMapper implements RecordMapper<Expense, List<?>, CSVFormat> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String filename;

    /**
     * Record format:
     * Kátik;Gifts, joy;CZK;-123.45;;Popis;2018-03-02 18:31:48;payee
     *
     * @param filename
     */
    public WalletSimpleExportMapper(String filename) {
        this.filename = filename + "_" + DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm").format(DateUtil.getCurrentDateTime()) + ".csv";
    }

    //account;category;currency;amount;ref_currency_amount;type;payment_type;payment_type_local;note;date;gps_latitude;gps_longitude;gps_accuracy_in_meters;warranty_in_month;transfer;payee;labels;envelope_id;custom_category
    @Override
    public CSVFormat getConfig() {
        return CSVFormat.DEFAULT.withDelimiter(';').withQuoteMode(QuoteMode.ALL).withQuote('"').withHeader("category", "currency", "expense", "income", "date", "payee");
    }

    @Override
    public List<?> map(Expense expense) {
        return Arrays.asList(expense.getCategory().getName(), expense.getCurrency(), getExpense(expense.getAmount()), getIncome(expense.getAmount()),
                convertDate(expense.getWhen()), expense.getVendor());
    }

    @Override
    public String getFileName() {
        return filename;
    }

    private Double getExpense(Double amount) {
        if (amount != null && amount < 0) {
            return amount;
        }
        return null;
    }

    private Double getIncome(Double amount) {
        if (amount != null && amount >= 0) {
            return amount;
        }
        return null;
    }

    private String convertDate(ZonedDateTime when) {
        return DATE_FORMATTER.format(when);
    }
}
