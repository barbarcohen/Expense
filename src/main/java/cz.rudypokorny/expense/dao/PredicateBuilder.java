package cz.rudypokorny.expense.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import cz.rudypokorny.expense.entity.ExpenseFilter;
import cz.rudypokorny.expense.model.QAccount;
import cz.rudypokorny.expense.model.QCategory;
import cz.rudypokorny.expense.model.QExpense;
import cz.rudypokorny.util.DateUtil;

import java.time.ZonedDateTime;

public class PredicateBuilder {

    public static Predicate fromExpenseFilter(ExpenseFilter expenseFilter) {
        BooleanBuilder builder = new BooleanBuilder();

        construct(builder, expenseFilter);

        return builder.getValue();
    }

    private static void construct(BooleanBuilder builder, ExpenseFilter expenseFilter) {
        builder.and(QAccount.account.eq(expenseFilter.getAccount()));

        if (expenseFilter.getCategory() != null) {
            builder.and(QCategory.category.eq(expenseFilter.getCategory()));
        }
        if (expenseFilter.getNote() != null) {
            builder.and(QExpense.expense.note.like(expenseFilter.getNote()));
        }
        if (expenseFilter.getDateTo() != null && expenseFilter.getDateFrom() != null) {
            //builder.and(QExpense.expense.when.goe(expenseFilter.getDateFrom().atZone(DateUtil.getCurrentTimeZone())));
            ZonedDateTime from = expenseFilter.getDateFrom().atZone(DateUtil.getCurrentTimeZone());
            System.out.println("FROM " + from);

            builder.and(QExpense.expense.when.between(from, expenseFilter.getDateTo().atZone(DateUtil.getCurrentTimeZone())));
        }
        if (expenseFilter.getAmountMax() != null) {
            builder.and(QExpense.expense.amount.loe(expenseFilter.getAmountMax()));
        }
        if (expenseFilter.getAmountMin() != null) {
            builder.and(QExpense.expense.amount.goe(expenseFilter.getAmountMin()));
        }
    }
}
