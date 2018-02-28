package cz.rudypokorny.expense;

import cz.rudypokorny.expense.converter.Converter;
import cz.rudypokorny.expense.converter.impl.CSVConverter;
import cz.rudypokorny.expense.converter.impl.KatikRecordMapper;
import cz.rudypokorny.expense.converter.impl.RudyRecordMapper;
import cz.rudypokorny.expense.model.Expense;
import org.apache.commons.csv.CSVFormat;

import java.util.List;

/**
 * Created by Rudolf on 28/02/2018.
 */
public class ConvertApplication {

    public static void main(String[] args) {
        Converter converterRudy = new CSVConverter(new RudyRecordMapper(), CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader());
        Converter converterKatik = new CSVConverter(new KatikRecordMapper(), CSVFormat.DEFAULT.withDelimiter(';'));

        List<Expense> expenseListRudy = converterRudy.convert();
        List<Expense> expenseListKatik = converterKatik.convert();
    }

}
