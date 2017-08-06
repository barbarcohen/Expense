package cz.rudypokorny.expense;


import cz.rudypokorny.expense.dao.AccountDao;
import cz.rudypokorny.expense.dao.UserDao;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.User;
import cz.rudypokorny.expense.service.IImportService;
import cz.rudypokorny.util.CategoryEnum;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.IOException;
import java.util.stream.Collectors;


@SpringBootApplication
@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class MainApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication app = new SpringApplication(MainApplication.class);

        app.run(args);
    }

    @Bean
    public Mapper mapper() {
        return new DozerBeanMapper();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {


            //TODO run only if import args
            if (args.length > 0) {
                User currentUser = User.create("user", "password");
                Account account = Account.named("default").activeFor(currentUser);

                ctx.getBean(AccountDao.class).save(account);
                ctx.getBean(UserDao.class).save(currentUser);

                IImportService importService = ctx.getBean(IImportService.class);

                //import all categories
                importService.importCategories(() -> {
                    return CategoryEnum.categories.stream().
                            map(e -> Category.named(e.getSubCategory()).withParent(e.getCategory())).
                            collect(Collectors.toList());
                });

                //rudolf
                //importService.importExpensesForAccount();

                //katka
            }

        };
    }

}
