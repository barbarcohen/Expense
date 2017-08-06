package cz.rudypokorny.expense.service.impl;

import cz.rudypokorny.expense.dao.AccountDao;
import cz.rudypokorny.expense.dao.CategoryDao;
import cz.rudypokorny.expense.dao.ExpenseDao;
import cz.rudypokorny.expense.dao.PredicateBuilder;
import cz.rudypokorny.expense.dto.CategoryDTO;
import cz.rudypokorny.expense.entity.ExpenseFilter;
import cz.rudypokorny.expense.entity.Result;
import cz.rudypokorny.expense.entity.Rules;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.service.IExpenseService;
import cz.rudypokorny.expense.validator.IValidator;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private Mapper mapper;

    private Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    @Override
    public Result<Expense> spend(Expense expense) {

        if (expense.getCategory() != null) {
            Category category = categoryDao.findOneByName(expense.getCategory().getName());
            logger.debug("Loading category: {}", category);
            expense.on(category);
        }


        Rules rules = expenseValidator.validateNew(expense);
        if (rules.areBroken()) {
            return Result.fail(rules);
        }
        return Result.create(expenseDao.save(expense));

    }

    @Override
    public Result<Category> categorize(Category category) {
        Rules rules = categoryValidator.validateNew(category);
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
        //FIXME validate filter
        Iterable<Expense> expenses = expenseDao.findAll(PredicateBuilder.fromExpenseFilter(filter));
        return Result.create(expenses);
    }

    @Override
    public Result<Iterable<Account>> findAccounts() {
        //TODO find for GROUP
        return Result.create(accountDao.findAll());
    }

    @Override
    public Result<Optional<Account>> getAccountDetails(Long id) {
        return Result.create(Optional.ofNullable(accountDao.findOne(id)));
    }

    @Override
    public Result<Iterable<CategoryDTO>> findAllCategories() {
        Iterable<Category> categories = categoryDao.findAll();

        return Result.create(mapCollection(categories, CategoryDTO.class));
    }

    @Override
    public Result<Account> newAccount(Account account) {
        Rules rules = accountValidator.validateNew(account);
        if (!rules.areBroken()) {
            return Result.create(accountDao.save(account));
        }
        return Result.fail(rules);

    }

    @Override
    public Result<Account> updateAccount(Account account) {
        Rules rules = accountValidator.validateUpdate(account);
        if (!rules.areBroken()) {

            return Result.create(accountDao.save(account));
        }
        return Result.fail(rules);
    }

    protected <T extends Object, S extends Object> Iterable<T> mapCollection(Iterable<S> sourceList, Class<T> type) {
        java.util.List<T> resultList = new ArrayList<T>();
        for (S sourceItem : sourceList) {
            resultList.add(mapper.map(sourceItem, type));
        }
        return resultList;
    }
}
