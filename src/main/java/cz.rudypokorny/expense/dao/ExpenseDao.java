package cz.rudypokorny.expense.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import cz.rudypokorny.expense.entity.ExpenseFilter;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.model.QExpense;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Repository
public interface ExpenseDao extends CrudRepository<Expense, Long> , QueryDslPredicateExecutor{

    Iterable<Expense> findByWhenBetween(ZonedDateTime from, ZonedDateTime to);

    Iterable<Expense> findByAccount(Account account);

}
