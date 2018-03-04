package cz.rudypokorny.expense.service.impl;

import cz.rudypokorny.expense.dao.CategoryDao;
import cz.rudypokorny.expense.entity.ImportSource;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.service.IExpenseService;
import cz.rudypokorny.expense.service.IImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class ImportService implements IImportService {

    private Logger logger = LoggerFactory.getLogger(ImportService.class);

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private IExpenseService expenseService;

    @Override
    public void importExpensesForAccount(Account account, ImportSource<Expense> importSource) {
        //TODO design importing better

        Objects.requireNonNull(importSource, "ImportSource cannot be null.");
        importSource.loadData().stream().
                forEach(e -> {
                    expenseService.spend(e);
                    logger.trace("Importing {}.", e);
                });
    }

    @Override
    public void importCategories(ImportSource<Category> importSource) {
        Objects.requireNonNull(importSource, "ImportSource cannot be null.");

        //only not saved already
        /**
        importSource.loadData().stream().
                filter(input -> StreamSupport.stream(categoryDao.findAll().spliterator(), false).
                        noneMatch(s -> s.getName().equals(input.getName()))).
                forEach(c -> {
                    if (c.getParent() == null) {
                        categoryDao.save(c);
                        logger.debug("Importing {}.", c);
                    } else {
                        Category parent = categoryDao.findOneByName(c.getParent().getName());
                        if (parent == null) {
                            categoryDao.save(c.getParent());
                            categoryDao.save(c);
                            logger.debug("Importing {}.", c);
                        } else {
                            Category cat = Category.namedWithParent(c.getName(), parent);
                            categoryDao.save(cat);
                            logger.debug("Importing {}.", cat);
                        }
                    }
                });
         **/
    }
}
