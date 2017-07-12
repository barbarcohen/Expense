package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rudolf on 10/07/2017.
 */
@Repository
public interface CategoryDao extends CrudRepository<Category, Long> {
}
