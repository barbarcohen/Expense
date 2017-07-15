package cz.rudypokorny.expense.entity;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.util.DateUtil;

import java.time.LocalDate;

public class ExpenseFilter {

    private static final LocalDate DEFAULT_DATE_FROM = DateUtil.getCurrentDate().minusDays(7);

    private final Account account;
    private final Category category;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final Double amountMax;
    private final Double amountMin;
    private final String note;


    private ExpenseFilter(Account account, Category category, LocalDate dateFrom, LocalDate dateTo, Double amountMax, Double amountMin, String note) {
        this.account = account;
        this.category = category;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.amountMax = amountMax;
        this.amountMin = amountMin;
        this.note = note;
    }

    public static ExpenseFilter forCategory(Category category) {
        return new ExpenseFilter(null, category, DEFAULT_DATE_FROM, null, null, null, null);
    }

    public static ExpenseFilter forDates(LocalDate from, LocalDate to) {
        return new ExpenseFilter(null, null, from, to, null, null, null);
    }

    public Account getAccount() {
        return account;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
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
