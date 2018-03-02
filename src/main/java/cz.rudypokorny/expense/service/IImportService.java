package cz.rudypokorny.expense.service;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.entity.ImportSource;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Record;

public interface IImportService {

    void importExpensesForAccount(Account account, ImportSource<Record> importSource);

    void importCategories(ImportSource<Category> importSource);
}
