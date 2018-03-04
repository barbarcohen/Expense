package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryDao extends CrudRepository<Category, Long> {

    Category findOneByName(String name);
}
