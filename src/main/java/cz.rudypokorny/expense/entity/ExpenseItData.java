package cz.rudypokorny.expense.entity;

import cz.rudypokorny.expense.model.Expense;

import java.util.List;

/**
 * Created by Rudolf on 02/08/2017.
 */
public class ExpenseItData implements ImportSource<Expense> {

    @Override
    public List<Expense> loadData() {
        return null;
    }

}
