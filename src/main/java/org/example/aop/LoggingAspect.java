package org.example.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Класс для логирования
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(org.example.service..*)")
    public void servicePointcut() {
    }

    /**
     * Логирует параметры вызова и выходное значение методов в пакете org.example.service.
     *
     * @param joinPoint объект, представляющий точку соединения (вызов метода)
     * @return результат выполнения метода
     * @throws Throwable если метод выбрасывает исключение
     */
    @Around("servicePointcut()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Вызов метода: {} с аргументами: {}", methodName, args);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("Исключение в методе: {} с аргументами: {}", methodName, args, throwable);
            throw throwable;
        }

        log.info("Метод {} вернул значение {}", methodName, result);

        return result;
    }

    /**
     * Логирует чтение из файла
     */
    @Before("execution(* org.example.repository.BookRepository.readAll(..))")
    public void logBeforeReadBooks() {
        log.info("чтение из файла");
    }
}
