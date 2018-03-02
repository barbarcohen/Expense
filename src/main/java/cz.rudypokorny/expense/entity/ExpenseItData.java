package cz.rudypokorny.expense.entity;

import cz.rudypokorny.expense.model.Record;

import java.util.List;

/**
 * Created by Rudolf on 02/08/2017.
 */
public class ExpenseItData implements ImportSource<Record> {

    @Override
    public List<Record> loadData() {
        return null;
    }

}
