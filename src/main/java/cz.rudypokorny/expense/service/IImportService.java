package cz.rudypokorny.expense.service;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.entity.ImportSource;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;

public interface IImportService {

    void importExpensesForAccount(Account account, ImportSource<Expense> importSource);

    void importCategories(ImportSource<Category> importSource);
}
