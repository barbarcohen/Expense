package cz.rudypokorny.expense;

import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum;
import cz.rudypokorny.expense.tools.importexport.importing.CSVImporter;
import cz.rudypokorny.expense.tools.importexport.importing.mappers.KatikCSVRecordMapper;
import cz.rudypokorny.expense.tools.importexport.importing.mappers.RudikCSVRecordMapper;
import cz.rudypokorny.expense.tools.statistics.RecordStatistics;
import cz.rudypokorny.util.DateUtil;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        //new CSVExporter(new WalletSimpleExportMapper("target/Wallet-Katik")).export(expenseListKatik);
        //new CSVExporter(new WalletSimpleExportMapper("target/Wallet-Rudik")).export(expenseListRudy);

        expenseListKatik.addAll(expenseListRudy);

        RecordStatistics rec = RecordStatistics.compute(expenseListKatik)
                .filterByCategory(CategoryEnum.VEHICLE_FUEL)
                .filterByDates(ZonedDateTime.of(2017,1,1,1,0,0,0, DateUtil.getCurrentTimeZone()).toInstant(),
                        ZonedDateTime.of(2018,1,1,1,0,0,0, DateUtil.getCurrentTimeZone()).toInstant());
        System.out.println("From: "+rec.from());
        System.out.println("To: "+rec.to());
        System.out.println("Avg: "+rec.average());
        System.out.println(rec.sum());
    }

}
