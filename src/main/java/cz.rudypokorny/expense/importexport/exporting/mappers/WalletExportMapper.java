package cz.rudypokorny.expense.importexport.exporting.mappers;

import cz.rudypokorny.expense.importexport.RecordMapper;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.util.DateUtil;
import org.apache.commons.csv.CSVFormat;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class WalletExportMapper implements RecordMapper<Expense, List<?>, CSVFormat> {

    public static final String EMPTY_STRING = "";
    public static final String TYPE_EXPENSES = "Expenses";
    public static final String TYPE_INCOME = "Incomes";
    public static final String PAYMENT_TYPE_CASH = "CASH";
    public static final String PAYMENT_TYPE_LOCAL_CASH = "Cash";
    public static final String ENVELOPE_ID = "2007";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String filename;

    /**
     * Record format:
     * Kátik;Gifts, joy;CZK;-123.45;-123.45;Expenses;CASH;Cash;Popis;2018-03-02 18:31:48;49.17629;16.55581;13;24;false;;Ok;2007;false
     *
     * @param filename
     */
    public WalletExportMapper(String filename) {
        this.filename = filename + "_" + DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm").format(DateUtil.getCurrentDateTime()) + ".csv";
    }


    //account;category;currency;amount;ref_currency_amount;type;payment_type;payment_type_local;note;date;gps_latitude;gps_longitude;gps_accuracy_in_meters;warranty_in_month;transfer;payee;labels;envelope_id;custom_category
    @Override
    public CSVFormat getConfig() {
        return CSVFormat.DEFAULT.withDelimiter(';').withHeader("account", "category", "currency", "amount", "ref_currency_amount", "type", "payment_type", "payment_type_local", "note", "date", "gps_latitude", "gps_longitude", "gps_accuracy_in_meters", "warranty_in_month", "transfer", "payee", "labels", "envelope_id", "custom_category");
    }

    @Override
    public List<?> map(Expense record) {
        String latitude, longtitude, accurancy, warranty, transfer, labels, envelopeId;
        latitude = longtitude = accurancy = warranty = transfer = labels = envelopeId = EMPTY_STRING;
        boolean customCategory =  true;

        //TODO fix category.geChilredn
        return Arrays.asList(record.getAccount().getName(), convertCategory(record.getCategory()), record.getCurrency(), record.getAmount(), record.getAmount(),
                convertType(record.getAmount()), PAYMENT_TYPE_CASH, PAYMENT_TYPE_LOCAL_CASH, record.getNote(), convertDate(record.getWhen()),
                latitude, longtitude, accurancy, transfer, record.getVendor(), labels, envelopeId, customCategory);
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

    private String convertType(Double amount) {
        if (amount != null && amount < 0) {
            return TYPE_EXPENSES;
        }
        return TYPE_INCOME;
    }

    private String convertDate(ZonedDateTime when) {
        return DATE_FORMATTER.format(when);
    }
}
