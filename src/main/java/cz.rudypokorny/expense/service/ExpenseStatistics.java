package cz.rudypokorny.expense.service;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Record;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpenseStatistics {

    private final List<Record> records;

    public static ExpenseStatistics compute(List<Record> records){
        return new ExpenseStatistics(records);
    }

    private ExpenseStatistics(List<Record> records) {
        this.records = Objects.requireNonNull(Collections.unmodifiableList(records), "'expenses' cannot be null");
    }

    public List<Record> getRecords() {
        return records;
    }


    public ExpenseStatistics filterByCategory(CategoryEnum categoryEnum) {
        return ret(records.stream().filter(expense -> categoryEnum.full().equals(expense.getCategory())).collect(Collectors.toList()));
    }

    public ExpenseStatistics filterByAccount(Account account) {
        //úTODO optional
        return ret(records.stream().filter(expense -> account.equals(expense.getAccount())).collect(Collectors.toList()));
    }

    private ExpenseStatistics ret(List<Record> results){
        return new ExpenseStatistics(results);
    }
}
