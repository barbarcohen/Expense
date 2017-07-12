package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountDao extends CrudRepository<Account, Long> {
}
