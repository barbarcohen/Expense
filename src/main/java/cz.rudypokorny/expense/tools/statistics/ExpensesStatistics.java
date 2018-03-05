package cz.rudypokorny.expense.tools.statistics;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpensesStatistics {

    private static final String NEWLINE = System.getProperty("line.separator");
    private final List<Expense> expenses;
    private ZonedDateTime from;
    private ZonedDateTime to;

    private ExpensesStatistics(List<Expense> expenses) {
        this.expenses = Objects.requireNonNull(Collections.unmodifiableList(expenses), "'expenses' cannot be null");
    }

    public static ExpensesStatistics none() {
        return new ExpensesStatistics(Collections.emptyList());
    }

    public static ExpensesStatistics compute(List<Expense> expenses) {
        return new ExpensesStatistics(expenses);
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
            //TODO extract sorter to static constant
            from = expenses.stream().sorted((o1, o2) -> o1.getWhen().compareTo(o2.getWhen())).map(Expense::getWhen).findFirst().orElse(null);
        }
        return from;
    }

    public ZonedDateTime to() {
        if (to == null) {
            //TODO extract to static constant
            to = expenses.stream().sorted((o1, o2) -> o2.getWhen().compareTo(o1.getWhen())).map(Expense::getWhen).findFirst().orElse(null);
        }
        return to;
    }

    public ExpensesStatistics filterExpenses() {
        return ret(expenses.stream().filter(expense -> expense.getAmount() < 0).collect(Collectors.toList()));
    }
    public ExpensesStatistics filterIncome() {
        return ret(expenses.stream().filter(expense -> expense.getAmount() > 0).collect(Collectors.toList()));
    }

    public ExpensesStatistics filterByCategory(CategoryEnum categoryEnum) {
        return ret(expenses.stream().filter(expense -> expense.getCategoryEnum().equals(categoryEnum)).collect(Collectors.toList()));
    }

    public ExpensesStatistics filterByDates(Instant from, Instant to) {
        return ret(expenses.stream()
                .filter(e -> isGreaterEqualThan(e, from) && isLessEqualThan(e, to))
                .collect(Collectors.toList()));
    }

    public ExpensesStatistics filterByAccount(Account account) {
        //todo optional
        return ret(expenses.stream().filter(expense -> account.equals(expense.getAccount())).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("RecordStatistics").append(NEWLINE)
                .append("Number of records: ").append(count()).append(NEWLINE)
                .append("From: ").append(from()).append(NEWLINE)
                .append("To: ").append(to()).append(NEWLINE)
                .append("Sum: ").append(sum()).append(NEWLINE)
                .append("Item average: ").append(average()).append(NEWLINE);

        //TODO print
        return buff.toString();
    }

    private ExpensesStatistics ret(List<Expense> results) {
        return new ExpensesStatistics(results);
    }

    private boolean isLessEqualThan(Expense expense, Instant instant) {
        if (instant == null) {
            return true;
        }
        return instant.compareTo(expense.getWhen().toInstant()) != -1;
    }

    private boolean isGreaterEqualThan(Expense expense, Instant instant) {
        if (instant == null) {
            return true;
        }
        return instant.compareTo(expense.getWhen().toInstant()) != 1;
    }
}
