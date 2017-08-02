package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryDao extends CrudRepository<Category, Long> {

    //main Category
    Iterable<Category> findByParentIsNull();

    //subCategory
    Iterable<Category> findByParentId(Long parentId);

    Iterable<Category> findByParentName(String name);

    Category findOneByName(String name);
}
