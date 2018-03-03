package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Expense;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface ExpenseDao extends CrudRepository<Expense, Long> , QueryDslPredicateExecutor{

    Iterable<Expense> findByWhenBetween(ZonedDateTime from, ZonedDateTime to);

    Iterable<Expense> findByAccount(Account account);

}
