package cz.rudypokorny.expense;


import cz.rudypokorny.expense.dao.AccountDao;
import cz.rudypokorny.expense.dao.UserDao;
import cz.rudypokorny.expense.entity.ContextUser;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.User;
import cz.rudypokorny.expense.service.IImportService;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

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
                ctx.getBean(UserDao.class).save(currentUser);

                //do importin
                Authentication auth = new TestingAuthenticationToken(
                        ContextUser.create(currentUser, AuthorityUtils.createAuthorityList("USER")),
                        currentUser.getPassword());
                auth.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(auth);

                Account account = Account.named("default").activeFor(currentUser);

                ctx.getBean(AccountDao.class).save(account);


                IImportService importService = ctx.getBean(IImportService.class);

                //import all domain
               /* importService.importCategories(() -> {
                    return CategoryEnum.domain.stream().
                            map(e -> Category.named(e.getSubCategory()).withParent(e.getName())).
                            collect(Collectors.toList());
                });*/

                //rudolf
                //importService.importExpensesForAccount();

                //katka
            }

        };
    }

}
