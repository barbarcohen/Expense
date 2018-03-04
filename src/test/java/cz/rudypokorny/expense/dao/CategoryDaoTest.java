package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@javax.transaction.Transactional
public class CategoryDaoTest {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCategoryWithNullParent() {
        String categoryName = "dishfsdhf hsdfj hsjghsdhg ";
        String parentCategoryName = "dishfsdhf hsdfj hsjsdflksdogk sg ghsdhg ";

        Category parentCategory = testEntityManager.persist(Category.named(parentCategoryName));

        //FIXME
        Category category = null;//testEntityManager.persist(Category.namedWithParent(categoryName, parentCategory));

        testEntityManager.clear();

        ArrayList<Category> categories = (ArrayList<Category>) categoryDao.findByParentIsNull();
        assertEquals(1, categories.size());
        assertEquals(parentCategory.getName(), categories.get(0).getName());

        categories = (ArrayList<Category>) categoryDao.findByParentName(parentCategory.getName());
        assertEquals(1, categories.size());
        assertEquals(category.getName(), categories.get(0).getName());
    }


}
