package cz.rudypokorny.expense;

import cz.rudypokorny.expense.model.Record;
import cz.rudypokorny.expense.service.ExpenseStatistics;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum;
import cz.rudypokorny.expense.tools.importexport.importing.CSVImporter;
import cz.rudypokorny.expense.tools.importexport.importing.mappers.KatikCSVRecordMapper;
import cz.rudypokorny.expense.tools.importexport.importing.mappers.RudikCSVRecordMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rudolf on 28/02/2018.
 */
public class ConvertApplication {

    public static void main(String[] args) throws IOException {

        List<Record> recordListRudy =  new CSVImporter(new RudikCSVRecordMapper()).load();
        List<Record> recordListKatik = new CSVImporter(new KatikCSVRecordMapper()).load();

        List<Record> all = new ArrayList<>();
        all.addAll(recordListKatik);
        all.addAll(recordListRudy);

        //new CSVExporter(new ExpenseItExportMapper("target/Expenses-rudik")).export(expenseListRudy);
        //new CSVExporter(new ExpenseItExportMapper("target/Expenses-katik")).export(expenseListKatik);
        //new CSVExporter(new ExpenseItExportMapper("target/Expenses-all")).export(all);
        //new CSVExporter(new WalletExportMapper("target/Wallet")).export(all);

        ExpenseStatistics.compute(all).filterByCategory(CategoryEnum.INCOME_OTHER).getRecords().size();
    }

}
