package cz.rudypokorny.expense.tools.statistics;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ExpensesStatistics {

    public static final String SPACE = " ";
    private static final String NEWLINE = System.getProperty("line.separator");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final Comparator<Expense> DATE_COMPARATOR = Comparator.comparing((Expense expense) -> expense.getWhen());
    private static final NumberFormat AMOUNT_FORMATTER = new DecimalFormat("#,###.00");
    private static final String CURRENCY = "CZK";
    private static final String EMPTY = "";
    private final List<Expense> expenses;
    private Set<CategoryEnum> categories;
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
            from = expenses.stream().sorted(DATE_COMPARATOR).map(Expense::getWhen).findFirst().orElse(null);
        }
        return from;
    }

    public ZonedDateTime to() {
        if (to == null) {
            to = expenses.stream().sorted(DATE_COMPARATOR.reversed()).map(Expense::getWhen).findFirst().orElse(null);
        }
        return to;
    }

    public Set<CategoryEnum> categories() {
        if (categories == null) {
            categories = expenses.stream().map(expense -> expense.getCategoryEnum()).distinct().collect(Collectors.toSet());
        }
        return categories;
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
                .append("From: ").append(formatDate(from())).append(NEWLINE)
                .append("To: ").append(formatDate(to())).append(NEWLINE)
                .append("Sum: ").append(formatAmount(sum())).append(NEWLINE)
                .append("Categories: ").append(formatCategories(categories())).append(NEWLINE)
                .append("Item average: ").append(formatAmount(average())).append(NEWLINE);
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

    private String formatDate(ZonedDateTime date) {
        if (date != null) {
            return DATE_FORMATTER.format(date);
        }
        return EMPTY;
    }

    private String formatAmount(Double amount) {
        if (amount != null) {
            return AMOUNT_FORMATTER.format(amount) + SPACE + CURRENCY;
        }
        return EMPTY;
    }

    private String formatCategories(Set<CategoryEnum> categories) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(NEWLINE).append("============").append(NEWLINE);
        categories.stream().forEach(e -> buffer.append(e).append(NEWLINE));
        return buffer.toString();
    }
}
