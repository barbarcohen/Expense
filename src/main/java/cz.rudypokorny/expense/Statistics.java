package cz.rudypokorny.expense;

import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.importing.CSVImporter;
import cz.rudypokorny.expense.tools.importexport.importing.mappers.KatikCSVRecordMapper;
import cz.rudypokorny.expense.tools.importexport.importing.mappers.RudikCSVRecordMapper;
import cz.rudypokorny.expense.tools.statistics.ExpensesStatistics;

import java.io.IOException;
import java.util.List;

/**
 * Created by Rudolf on 28/02/2018.
 */
public class Statistics {

    public static void main(String[] args) throws IOException {

        List<Expense> expenseListRudy =  new CSVImporter(new RudikCSVRecordMapper()).load();
        List<Expense> expenseListKatik = new CSVImporter(new KatikCSVRecordMapper()).load();


        ExpensesStatistics rec = ExpensesStatistics.compute(expenseListKatik)
                .filterExpenses();

        System.out.println(rec);
    }

}
