package cz.rudypokorny.expense.service.impl;

import cz.rudypokorny.expense.dao.AccountDao;
import cz.rudypokorny.expense.dao.CategoryDao;
import cz.rudypokorny.expense.dao.ExpenseDao;
import cz.rudypokorny.expense.entity.ExpenseFilter;
import cz.rudypokorny.expense.entity.Result;
import cz.rudypokorny.expense.entity.Rules;
import cz.rudypokorny.expense.entity.Validable;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.service.IExpenseService;
import cz.rudypokorny.expense.validator.IValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Optional;

@Service
public class ExpenseService implements IExpenseService {
    @Autowired
    private ExpenseDao expenseDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    @Qualifier("expenseValidator")
    private IValidator<Expense> expenseValidator;

    @Autowired
    @Qualifier("categoryValidator")
    private IValidator<Category> categoryValidator;

    @Autowired
    @Qualifier("accountValidator")
    private IValidator<Account> accountValidator;

    @Autowired
    private ApplicationContext applicationContext;

    private Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    @Override
    public Result<Expense> spend(Expense expense) {
        Rules rules = expenseValidator.validate(expense);
        if (rules.areBroken()) {
            return Result.fail(rules);
        } else {
            return Result.create(expenseDao.save(expense));
        }
    }

    @Override
    public Result<Category> categorize(Category category) {
        Rules rules = categoryValidator.validate(category);
        if (rules.areBroken()) {
            return Result.fail(rules);
        }
        return Result.create(categoryDao.save(category));
    }

    @Override
    public Result<Optional<Expense>> getExpense(Long id) {
        return Result.create(Optional.ofNullable(expenseDao.findOne(id)));
    }

    @Override
    public Result<Iterable<Expense>> findExpenseByFilter(ExpenseFilter filter) {
        throw new NotImplementedException();
    }

    @Override
    public Result<Iterable<Account>> findAccounts() {
        return Result.create(accountDao.findAll());
    }

    @Override
    public Result<Optional<Account>> getAccount(Long id) {
        return Result.create(Optional.ofNullable(accountDao.findOne(id)));
    }

    @Override
    public Result<Iterable<Category>> getCategories() {
        return Result.create(categoryDao.findAll());
    }

    @Override
    public Result<Account> newAccount(Account account) {
        Rules rules = accountValidator.validate(account);
        if (!rules.areBroken()) {
            return Result.create(accountDao.save(account));
        }
        return Result.fail(rules);

    }

    private IValidator<?> findValidator(Validable validable) {
        //TODO find out
        return null;
    }
}
