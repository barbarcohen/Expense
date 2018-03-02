package cz.rudypokorny.expense.importexport.exporting;

import cz.rudypokorny.expense.model.Expense;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Rudolf on 01/03/2018.
 */
public class CSVExporter {


    private static final Logger logger = LoggerFactory.getLogger(CSVExporter.class);
    private static final String DATEFORMAT = "dd/MM/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATEFORMAT);
    private final String fileName;

    public CSVExporter(String fileName) {
        this.fileName = Objects.requireNonNull(fileName, "filename cannot be null");
    }

    //TODO use mapper as well
    public void export(List<Expense> expenses) throws IOException {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withDelimiter(',').withQuoteMode(QuoteMode.ALL).withQuote('"')
                        .withHeader("Date", "Type", "Category", "Subcategory", "Vendor", "Payment", "Currency", "Amount", "Note", "ID"));
        ) {
            logger.debug("Exporting {} expenses to {}.", expenses.size(), fileName);

            //"02/08/2015","Rudík","Investments","Life Insurance","Aegon","Account Transfer","CZK","-1 150","","10BC8A31-3834-4286-9E5F-B0D0D124E265"
            for (final Expense expense : expenses) {

                String date = DATE_FORMATTER.format(expense.getWhen());

                //TODO fix category.geChilredn
                csvPrinter.printRecord(Arrays.asList(date, expense.getAccount().getName(), expense.getCategory().getParent().getName(), expense.getCategory().getName(),
                        expense.getVendor(), expense.getPayment(), expense.getCurrency(), expense.getAmount(), expense.getNote(), expense.getExtId()));
            }

            csvPrinter.flush();
        }
    }
}
