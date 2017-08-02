package cz.rudypokorny.expense.service.impl;

import cz.rudypokorny.expense.dao.CategoryDao;
import cz.rudypokorny.expense.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by Rudolf on 01/08/2017.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public Category findOrCreate(String name) {
        Objects.requireNonNull(name);
        Category found = categoryDao.findOneByName(name);
        if (found == null) {
            categoryDao.save(Category.named(name));
        }
        return found;
    }
}
