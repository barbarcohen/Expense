package cz.rudypokorny.expense.service;

import cz.rudypokorny.expense.entity.ExpenseFilter;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.entity.Result;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Transactional
public interface IExpenseService {

    Result<Expense> spend(@Valid  final Expense expense);

    Result<Optional<Expense>> getExpense(final Long id);

    Result<Iterable<Expense>> findExpenseByFilter(final ExpenseFilter filter);

    Result<Iterable<Account>> findAccounts();

    Result<Optional<Account>> getAccount(Long id);

    Result<Iterable<Category>> getCategories();

    Result<Category> categorize(@Valid Category category);

    Result<Account> newAccount(@Valid final Account account);
}
