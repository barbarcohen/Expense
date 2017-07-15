package cz.rudypokorny.expense;


import cz.rudypokorny.expense.service.IExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.shell.Bootstrap;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class MainApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication app = new SpringApplication(MainApplication.class);

        app.run(args);
    }

}
