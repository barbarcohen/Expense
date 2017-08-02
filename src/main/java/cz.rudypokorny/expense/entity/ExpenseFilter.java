package cz.rudypokorny.expense.entity;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.util.DateUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;

public final class ExpenseFilter {

    private final Account account;
    private final Category category;
    private final LocalDateTime dateFrom;
    private final LocalDateTime dateTo;
    private final Double amountMax;
    private final Double amountMin;
    private final String note;

    private ExpenseFilter(Account account, Category category, LocalDateTime dateFrom, LocalDateTime dateTo, Double amountMax, Double amountMin, String note) {
        this.account = account;
        this.category = category;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.amountMax = amountMax;
        this.amountMin = amountMin;
        this.note = note;
    }

    public static final LocalDateTime getDefaultDateFrom() {
        return DateUtil.getCurrentDate().minusDays(7).atStartOfDay();
    }

    public static final LocalDateTime getDefaultDateTo() {
        return DateUtil.getCurrentDate().atTime(LocalTime.MAX);
    }

    public static ExpenseFilter create(Account account) {
        return new ExpenseFilter(account, null, getDefaultDateFrom(), getDefaultDateTo(), null, null, null);
    }

    public ExpenseFilter amountMBetween(Double amountMin, Double amountMax) {
        return new ExpenseFilter(account, null, getDefaultDateFrom(), getDefaultDateTo(), amountMax, amountMin, null);
    }

    public ExpenseFilter forCategory(Category category) {
        return new ExpenseFilter(account, category, dateFrom, dateTo, amountMax, amountMin, note);
    }

    public ExpenseFilter forDates(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return new ExpenseFilter(account, category, dateFrom, dateTo, amountMax, amountMin, note);
    }

    public ExpenseFilter today() {
        return new ExpenseFilter(account, category, DateUtil.getCurrentDate().atStartOfDay(), getDefaultDateTo(), amountMax, amountMin, note);
    }

    public ExpenseFilter withNote(String note) {
        return new ExpenseFilter(account, category, dateFrom, dateTo, amountMax, amountMin, note);
    }

    public Account getAccount() {
        return account;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public Double getAmountMax() {
        return amountMax;
    }

    public Double getAmountMin() {
        return amountMin;
    }

    public String getNote() {
        return note;
    }
}
