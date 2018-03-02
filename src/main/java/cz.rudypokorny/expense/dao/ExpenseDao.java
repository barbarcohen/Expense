package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Record;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface ExpenseDao extends CrudRepository<Record, Long> , QueryDslPredicateExecutor{

    Iterable<Record> findByWhenBetween(ZonedDateTime from, ZonedDateTime to);

    Iterable<Record> findByAccount(Account account);

}
