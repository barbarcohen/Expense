package cz.rudypokorny.expense;

import cz.rudypokorny.exception.AccountAwareException;
import cz.rudypokorny.expense.dao.AccountDao;
import cz.rudypokorny.expense.entity.AccountAware;
import cz.rudypokorny.expense.entity.ContextUser;
import cz.rudypokorny.expense.entity.Result;
import cz.rudypokorny.expense.entity.Rules;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLayerAspect {

    @Autowired
    private AccountDao accountDao;

    private Logger logger = LoggerFactory.getLogger(ServiceLayerAspect.class);

    @Pointcut("execution(public cz.rudypokorny.expense.entity.Result<*> cz.rudypokorny.expense.service.*.*(..))")
    public void selectServiceMethodsReturnigClassResult() {
    }

    @Pointcut("execution(public * cz.rudypokorny.expense.service.*.*(cz.rudypokorny.expense.entity.AccountAware+,..))")
    public void selectServiceMethodsWithCategoryAwareSublassAsParameter() {
    }

    @Order(1)
    @Around("selectServiceMethodsReturnigClassResult()")
    public Object catchExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Catching exception on {}.{}, wrapping to Result.fail() {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), e);
            return Result.fail(new Rules().exception(e));
        }
    }

    @Order(2)
    @Around("selectServiceMethodsWithCategoryAwareSublassAsParameter()")
    public Object injectAccount(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof AccountAware) {

                    User currentUser = ((ContextUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
                    AccountAware object = ((AccountAware) arg);

                    if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                            hasAccount(currentUser) &&
                            checkExpenseAccount(object.getAccount(), currentUser.getActiveAccount())) {
                        object.setAccount(currentUser.getActiveAccount());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Catching exception on {}.{} when injecting Account, forwarding exception {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), e);
            throw e;
        }
        return joinPoint.proceed();
    }

    // => if account is present on object it must be the same
    private boolean hasAccount(User currentUser) {
        if (currentUser.getActiveAccount() == null) {
            throw new AccountAwareException(String.format("User %s must have an active account", currentUser));
        }
        return true;
    }

    private boolean checkExpenseAccount(Account objectAccount, Account currentAccount) {
        if (objectAccount!= null && !currentAccount.equals(objectAccount)) {
            throw new AccountAwareException(String.format("Account cannot be changed % -> %", objectAccount, currentAccount));
        }
        return true;
    }
}
