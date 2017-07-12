package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseDao extends CrudRepository<Expense, Long> {
}
