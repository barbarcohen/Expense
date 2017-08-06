package cz.rudypokorny.expense.dao;

import cz.rudypokorny.expense.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rudolf on 03/08/2017.
 */
@Repository
public interface UserDao extends CrudRepository<User, Long> {

    User findByName(String name);
}
