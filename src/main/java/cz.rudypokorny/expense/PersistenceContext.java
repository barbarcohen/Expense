package cz.rudypokorny.expense;

import com.jolbox.bonecp.BoneCPDataSource;
import cz.rudypokorny.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
@PropertySource("classpath:application.properties")
@EntityScan("cz.rudypokorny.expense.model")
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
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
}