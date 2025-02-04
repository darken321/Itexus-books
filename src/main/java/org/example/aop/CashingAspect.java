package org.example.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.example.model.Book;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для кеширования данных о книге, чтоб не читать ее повторно с диска
 * Запись в кеш и выдача из него происходят, когда книгу ищут по названию.
 * Кеш очищается при изменении данных по книгам.
 */
@Aspect
@Component
@Slf4j
public class CashingAspect {

    private static final Map<String, List<Book>> cache = new HashMap<>();

    @Pointcut("execution(* org.example.service.BookService.findByName(..))")
    public void findBooksPointcut() {
    }

    /**
     * Кэширует вызовы метода findBooksByName.
     * Этот метод оборачивает вызовы метода findBooksByName в кэширование. Если результат для
     * заданного имени книги уже находится в кэше, он возвращается без выполнения метода.
     * В противном случае метод выполняется, и его результат сохраняется в кэше.
     *
     * @param joinPoint объект, представляющий точку соединения (вызов метода)
     * @return результат выполнения метода, либо закэшированный результат, если он уже существует
     * @throws Throwable если метод выбрасывает исключение
     */
    @Around("findBooksPointcut()")
    public Object cacheAroundService(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        String bookName = (String) args[0];

        if (cache.containsKey(bookName)) {
            log.info("Книга {} найдена в кэше", bookName);
            return cache.get(bookName);
        }

        Object result = joinPoint.proceed();

        if (result instanceof List<?> resultList) {
            if (!resultList.isEmpty() && resultList.get(0) instanceof Book) {
                cache.put(bookName, (List<Book>) result);
                log.info("Книга добавлена в кэш: {}", bookName);
            }
        }

        return result;
    }

    /**
     * Стирает информацию из кеша в случае изменения, добавления или удаления книги.
     */
    @AfterReturning(
            "execution(* org.example.service.BookService.add(..)) || " +
            "execution (* org.example.service.BookService.edit(..)) ||" +
            "execution(* org.example.service.BookService.delete(..))")
    public void clearCacheAdvice() {
        log.info("Стираю кеш");
        cache.clear();
    }
}