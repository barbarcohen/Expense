package cz.rudypokorny.expense;

import cz.rudypokorny.expense.importexport.exporting.CSVExporter;
import cz.rudypokorny.expense.importexport.exporting.mappers.ExpenseItExportMapper;
import cz.rudypokorny.expense.importexport.exporting.mappers.WalletExportMapper;
import cz.rudypokorny.expense.importexport.importing.CSVImporter;
import cz.rudypokorny.expense.importexport.importing.mappers.KatikCSVRecordMapper;
import cz.rudypokorny.expense.importexport.importing.mappers.RudikCSVRecordMapper;
import cz.rudypokorny.expense.model.Expense;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rudolf on 28/02/2018.
 */
public class ConvertApplication {

    public static void main(String[] args) throws IOException {

        List<Expense> expenseListRudy =  new CSVImporter(new RudikCSVRecordMapper()).load();
        List<Expense> expenseListKatik = new CSVImporter(new KatikCSVRecordMapper()).load();

        List<Expense> all = new ArrayList<>();
        all.addAll(expenseListKatik);
        all.addAll(expenseListRudy);

        //new CSVExporter(new ExpenseItExportMapper("target/Expenses-rudik")).export(expenseListRudy);
        //new CSVExporter(new ExpenseItExportMapper("target/Expenses-katik")).export(expenseListKatik);
        //new CSVExporter(new ExpenseItExportMapper("target/Expenses-all")).export(all);
        new CSVExporter(new WalletExportMapper("target/Wallet")).export(all);
    }

}
