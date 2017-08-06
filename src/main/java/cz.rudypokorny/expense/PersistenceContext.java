package cz.rudypokorny.expense;

import com.jolbox.bonecp.BoneCPDataSource;
import cz.rudypokorny.expense.entity.ContextUser;
import cz.rudypokorny.expense.model.User;
import cz.rudypokorny.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
@PropertySource("classpath:application.properties")
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider", auditorAwareRef = "auditorAware")
public class PersistenceContext {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(environment.getRequiredProperty("spring.datasource.driverClassName"));
        dataSource.setJdbcUrl(environment.getRequiredProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        return dataSource;
    }

    @Bean
    DateTimeProvider dateTimeProvider() {
        return () -> GregorianCalendar.from(DateUtil.getCurrentDateTime());
    }

    @Bean
    AuditorAware<User> auditorAware() {
        return new AuditorAware<User>() {
            @Override
            public User getCurrentAuditor() {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication == null || !authentication.isAuthenticated()) {
                    return null;
                }

                return ((ContextUser) authentication.getPrincipal()).getUser();
            }
        };
    }
}