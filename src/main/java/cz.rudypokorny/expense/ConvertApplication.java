package cz.rudypokorny.expense;

import cz.rudypokorny.expense.importexport.exporting.CSVExporter;
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

        CSVExporter exporter = new CSVExporter("Katik-expenses-28022018.csv");
        CSVExporter exporterRudik = new CSVExporter("Rudik-expenses-28022018.csv");
        CSVExporter exporterAll = new CSVExporter("All-expenses-28022018.csv");

        exporter.export(expenseListKatik);
        exporterRudik.export(expenseListRudy);
        exporterAll.export(all);
    }

}
