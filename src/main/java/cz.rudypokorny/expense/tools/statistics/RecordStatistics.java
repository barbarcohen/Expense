package cz.rudypokorny.expense.tools.statistics;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecordStatistics {

    private final List<Expense> expenses;
    private ZonedDateTime from;
    private ZonedDateTime to;

    private RecordStatistics(List<Expense> expenses) {
        this.expenses = Objects.requireNonNull(Collections.unmodifiableList(expenses), "'expenses' cannot be null");
    }

    public static RecordStatistics none() {
        return new RecordStatistics(Collections.emptyList());
    }

    public static RecordStatistics compute(List<Expense> expenses) {
        return new RecordStatistics(expenses);
    }

    public List<Expense> records() {
        return expenses;
    }

    public int count() {
        return expenses.size();
    }

    public Double sum() {
        return expenses.stream().collect(Collectors.summingDouble(Expense::getAmount));
    }

    public Double average() {
        return expenses.stream().collect(Collectors.averagingDouble(Expense::getAmount));
    }

    public ZonedDateTime from() {
        if (from == null) {
            //TODO extract to static constant
            from = expenses.stream().sorted((o1, o2) -> o1.getWhen().compareTo(o2.getWhen())).map(Expense::getWhen).findFirst().orElse(null);
        }
        return from;
    }

    //TODO to()


    public RecordStatistics filterByCategory(CategoryEnum categoryEnum) {
        return ret(expenses.stream().filter(expense -> categoryEnum.full().equals(expense.getCategory())).collect(Collectors.toList()));
    }


    public RecordStatistics filterByCategoryName(String categoryName) {
        return ret(expenses.stream().filter(expense -> checkCategory(expense.getCategory(), categoryName)).collect(Collectors.toList()));
    }

    public RecordStatistics filterByAccount(Account account) {
        //TODO optional
        return ret(expenses.stream().filter(expense -> account.equals(expense.getAccount())).collect(Collectors.toList()));
    }

    private RecordStatistics ret(List<Expense> results) {
        return new RecordStatistics(results);
    }

    private boolean checkCategory(Category category, String name) {
        return category.getParent().getName().equalsIgnoreCase(name) || category.getName().equalsIgnoreCase(name);
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("RecordStatistics \\n").append("Sum: ").append(sum()).append("Items found: ").append(count())
                .append("Found records").append("/n").append(records());

        //TODO print
        return buff.toString();
    }
}
