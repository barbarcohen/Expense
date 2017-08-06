package cz.rudypokorny.expense.entity;

import cz.rudypokorny.expense.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Rudolf on 18/02/17.
 */
public class ContextUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    private ContextUser(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getName(), user.getPassword(), authorities);
        this.user = user;
    }

    public static ContextUser create(final User user, Collection<? extends GrantedAuthority> authorities) {
        return new ContextUser(user, authorities);
    }

    public Long getId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }
}
