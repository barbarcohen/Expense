package cz.rudypokorny.expense.importexport.exporting.mappers;

import cz.rudypokorny.expense.importexport.RecordMapper;
import cz.rudypokorny.expense.model.Expense;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class IExpenseItExportMapper implements RecordMapper<Expense, CSVRecord, CSVFormat> {
    @Override
    public CSVFormat getConfig() {
        return null;
    }

    @Override
    public CSVRecord map(Expense csvRecord) {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }
}
