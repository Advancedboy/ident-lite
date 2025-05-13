package com.identlite.api.security;

import com.identlite.api.exceptions.LoggingAspectException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.identlite.api.controller..*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        logger.info("Вызов метода: {} с аргументами: {}", method, args);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long time = System.currentTimeMillis() - start;

            logger.info("Метод {} завершён за {} мс. Результат: {}", method, time, result);
            return result;
        } catch (Throwable ex) {
            long time = System.currentTimeMillis() - start;

            logger.error("Метод {} завершился с ошибкой через {} мс: {}", method, time, ex.getMessage(), ex);
            throw new LoggingAspectException("Ошибка при выполнении метода " + method, ex);
        }
    }
}
