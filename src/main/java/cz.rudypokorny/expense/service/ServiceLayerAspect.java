package cz.rudypokorny.expense.service;

import cz.rudypokorny.expense.entity.Result;
import cz.rudypokorny.expense.entity.Rules;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLayerAspect {

    private Logger logger = LoggerFactory.getLogger(ServiceLayerAspect.class);

    @Pointcut("execution(public cz.rudypokorny.expense.entity.Result<*> cz.rudypokorny.expense.service.*.*(..))")
    public void selectAllReturnigResult(){}


    @Around("selectAllReturnigResult()")
    public Object catchExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try{
            return joinPoint.proceed();
        } catch (Exception e){
            logger.error("Catching exception on {}.{}, wrapping to Result.fail() {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), e);
            return Result.fail(new Rules().exception(e));
        }
    }
}
