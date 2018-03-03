package cz.rudypokorny.expense.tools.importexport.exporting;

import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.RecordMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class CSVExporter {

    private static final Logger logger = LoggerFactory.getLogger(CSVExporter.class);

    private final RecordMapper<Expense, List<?>, CSVFormat> recordMapper;

    public CSVExporter(RecordMapper recordMapper) {
        this.recordMapper = Objects.requireNonNull(recordMapper, "recordMapper cannot be null");
    }

    public void export(List<Expense> expenses) throws IOException {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(recordMapper.getFileName()));
                CSVPrinter csvPrinter = new CSVPrinter(writer, recordMapper.getConfig());
        ) {
            logger.debug("Exporting {} expenses to {}.", expenses.size(), recordMapper.getFileName());

            expenses.stream().forEach(expense -> {
                try {
                    csvPrinter.printRecord(recordMapper.map(expense));
                } catch (IOException e) {
                    logger.error("Exporting error '{}' for record {}", e.getMessage(), expense);
                }
            });

            csvPrinter.flush();
        }
    }
}
