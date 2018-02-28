package cz.rudypokorny.expense.config;

import cz.rudypokorny.expense.dao.UserDao;
import cz.rudypokorny.expense.entity.ContextUser;
import cz.rudypokorny.expense.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Rudolf on 25/02/17.
 */
@Configuration
public class SecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private UserDao userDao;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                User user = userDao.findByName(s);
                if(user !=null){
                    return ContextUser.create(user, AuthorityUtils.createAuthorityList("USER"));
                } else {
                    throw new UsernameNotFoundException("Username '"+s+"' not found");
                }
            }
        };
    }
}
