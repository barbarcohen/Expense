package cz.rudypokorny.expense.service;

import cz.rudypokorny.expense.dto.CategoryDTO;
import cz.rudypokorny.expense.entity.ExpenseFilter;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Record;
import cz.rudypokorny.expense.entity.Result;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface IExpenseService {

    Result<Record> spend(final Record record);

    Result<Optional<Record>> getExpense(final Long id);

    Result<Iterable<Record>> findExpenseByFilter(final ExpenseFilter filter);

    Result<Iterable<Account>> findAccounts();

    Result<Optional<Account>> getAccountDetails(Long id);

    Result<Iterable<CategoryDTO>> findAllCategories();

    Result<Category> categorize(Category category);

    Result<Account> newAccount(final Account account);

    Result<Account> updateAccount(final Account account);
}
