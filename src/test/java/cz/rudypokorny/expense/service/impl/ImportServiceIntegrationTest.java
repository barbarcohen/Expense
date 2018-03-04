package cz.rudypokorny.expense.service.impl;

import cz.rudypokorny.expense.dao.CategoryDao;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.service.IImportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@javax.transaction.Transactional
public class ImportServiceIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private IImportService importService;

    @Test(expected = NullPointerException.class)
    public void testImportCategoriesWithNull() {
        importService.importCategories(null);
    }

    @Test
    public void testImportCategoriesAlreadySaved() {

        testEntityManager.persist(Category.named("abc"));
        testEntityManager.clear();

        importService.importCategories(() -> {
            return Arrays.asList(Category.named("abc"));
        });

        ArrayList<Category> result = (ArrayList<Category>) categoryDao.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("abc", result.get(0).getName());
    }

    @Test
    public void testImportCategoriesWithoutParents() {

        importService.importCategories(() -> {
            return Arrays.asList(Category.named("abc"));
        });
        testEntityManager.clear();

        Category abc = categoryDao.findOneByName("abc");
        assertNotNull(abc);
        assertEquals("abc", abc.getName());
    }

}