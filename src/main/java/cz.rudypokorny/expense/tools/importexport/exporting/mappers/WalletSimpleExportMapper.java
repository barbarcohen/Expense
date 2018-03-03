package cz.rudypokorny.expense.tools.importexport.exporting.mappers;

import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.RecordMapper;
import cz.rudypokorny.util.DateUtil;
import org.apache.commons.csv.CSVFormat;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class WalletSimpleExportMapper implements RecordMapper<Expense, List<?>, CSVFormat> {

    public static final String EMPTY_STRING = "";
    public static final String TYPE_EXPENSES = "Expenses";
    public static final String TYPE_INCOME = "Incomes";
    public static final String PAYMENT_TYPE_CASH = "CASH";
    public static final String PAYMENT_TYPE_LOCAL_CASH = "Cash";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String filename;

    /**
     * Record format:
     * Kátik;Gifts, joy;CZK;-123.45;-123.45;Expenses;CASH;Cash;Popis;2018-03-02 18:31:48;49.17629;16.55581;13;24;false;;Ok;2007;false
     *
     * @param filename
     */
    public WalletSimpleExportMapper(String filename) {
        this.filename = filename + "_" + DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm").format(DateUtil.getCurrentDateTime()) + ".csv";
    }


    //account;category;currency;amount;ref_currency_amount;type;payment_type;payment_type_local;note;date;gps_latitude;gps_longitude;gps_accuracy_in_meters;warranty_in_month;transfer;payee;labels;envelope_id;custom_category
    @Override
    public CSVFormat getConfig() {
        return CSVFormat.DEFAULT.withDelimiter(';').withHeader("account", "category", "currency", "expense", "income", "note", "date", "payee");
    }

    @Override
    public List<?> map(Expense expense) {
        return Arrays.asList(expense.getAccount().getName(), convertCategory(expense.getCategory()), expense.getCurrency(), getExpense(expense.getAmount()), getIncome(expense.getAmount()),
                 expense.getNote(), convertDate(expense.getWhen()), expense.getVendor());
    }

    @Override
    public String getFileName() {
        return filename;
    }

    private String convertCategory(final Category category) {
        if (category != null) {
            return category.getParent().getName() + ", " + category.getName();
        }
        return EMPTY_STRING;
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
