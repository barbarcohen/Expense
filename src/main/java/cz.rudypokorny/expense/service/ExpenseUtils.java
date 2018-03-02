package cz.rudypokorny.expense.service;

import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Record;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Rudolf on 01/03/2018.
 */
public class ExpenseUtils {

    public static DoubleSummaryStatistics sumTotals(List<Record> records){
        Objects.requireNonNull(records);
        return records.stream().mapToDouble(Record::getAmount).summaryStatistics();
    }

    public static List<Category> distinctiveCategories(List<Record> records){
        Objects.requireNonNull(records);
        return records.stream().map(e -> e.getCategory()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).
                entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());

    }


}
