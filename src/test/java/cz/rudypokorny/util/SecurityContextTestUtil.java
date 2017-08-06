package cz.rudypokorny.util;

import cz.rudypokorny.expense.entity.ContextUser;
import cz.rudypokorny.expense.model.User;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Rudolf on 03/08/2017.
 */
public class SecurityContextTestUtil {

    public static User addToSecurityContext(User currentUser) {
        Authentication auth = new TestingAuthenticationToken(
                ContextUser.create(currentUser, AuthorityUtils.createAuthorityList("USER")),
                currentUser.getPassword());
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return currentUser;
    }
}
