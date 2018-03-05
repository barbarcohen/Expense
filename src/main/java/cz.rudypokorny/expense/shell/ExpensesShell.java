package cz.rudypokorny.expense.shell;

import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.importing.CSVImporter;
import cz.rudypokorny.expense.tools.importexport.importing.mappers.KatikCSVRecordMapper;
import cz.rudypokorny.expense.tools.importexport.importing.mappers.RudikCSVRecordMapper;
import cz.rudypokorny.expense.tools.statistics.ExpensesStatistics;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellOption;

import java.util.*;

@ShellComponent
public class ExpensesShell {

    private static final Set<String> VALID_KEYS = new HashSet<>(Arrays.asList("cat, acc, amount"));

    public String find(@ShellOption String key) {
        ExpensesStatistics statistics = ExpensesStatistics.none();

        return statistics.toString();
    }


    //TODO temporary
    public List<Expense> load() {
        List<Expense> expenseListRudy = new CSVImporter(new RudikCSVRecordMapper()).load();
        List<Expense> expenseListKatik = new CSVImporter(new KatikCSVRecordMapper()).load();

        List<Expense> all = new ArrayList<>();
        all.addAll(expenseListKatik);
        all.addAll(expenseListRudy);
        return all;
    }
}
