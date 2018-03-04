package cz.rudypokorny.expense;

import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.exporting.CSVExporter;
import cz.rudypokorny.expense.tools.importexport.exporting.mappers.WalletSimpleExportMapper;
import cz.rudypokorny.expense.tools.importexport.importing.CSVImporter;
import cz.rudypokorny.expense.tools.importexport.importing.mappers.KatikCSVRecordMapper;
import cz.rudypokorny.expense.tools.importexport.importing.mappers.RudikCSVRecordMapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Rudolf on 28/02/2018.
 */
public class ConvertApplication {

    public static void main(String[] args) throws IOException {

        List<Expense> expenseListRudy =  new CSVImporter(new RudikCSVRecordMapper()).load();
        List<Expense> expenseListKatik = new CSVImporter(new KatikCSVRecordMapper()).load();

        //new CSVExporter(new ExpenseItExportMapper("target/Expenses-rudik")).export(expenseListRudy);
        //new CSVExporter(new ExpenseItExportMapper("target/Expenses-katik")).export(expenseListKatik);
        //new CSVExporter(new ExpenseItExportMapper("target/Expenses-all")).export(all);
        new CSVExporter(new WalletSimpleExportMapper("target/Wallet-Katik")).export(expenseListKatik);
        new CSVExporter(new WalletSimpleExportMapper("target/Wallet-Rudik")).export(expenseListRudy);

        //System.out.println(RecordStatistics.compute(all).filterByCategory(CategoryEnum.INCOME_OTHER).toString());
    }

}
