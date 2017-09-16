package pl.sdacademy.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

    @Before(value = "execution(public * pl.sdacademy.infrastructure.configuration.DemoExceptionHandler.internalServer500(..))")
    public void logInternalServerException(JoinPoint joinPoint) {
        log.error("Unrecoverable exception was thrown", (Exception) joinPoint.getArgs()[0]);
    }

    @Before(value = "execution(public * pl.sdacademy.infrastructure.configuration.DemoExceptionHandler.*(..))" +
                    "&& ! execution(public * pl.sdacademy.infrastructure.configuration.DemoExceptionHandler.internalServer500(..))")
    public void logException(JoinPoint joinPoint) {
        log.info("", (Exception) joinPoint.getArgs()[0]);
    }
}
