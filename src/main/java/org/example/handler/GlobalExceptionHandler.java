package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.example.DTO.ExceptionDto;
import org.example.mapper.ExceptionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

/**
 * Глобальный обработчик исключений для приложения.
 * Этот класс перехватывает исключения, возникающие в приложении, и преобразует их в объекты {@link ExceptionDto}.
 * Использует {@link ExceptionMapper} для преобразования исключений.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionMapper exceptionMapper;

    /**
     * Обрабатывает все исключения типа {@link Exception}.
     *
     * @param exception исключение, которое было выброшено
     * @return объект {@link ExceptionDto}, содержащий информацию об исключении
     */
    @ExceptionHandler(Exception.class)
    public ExceptionDto handleException(Exception exception) {
        return exceptionMapper.exceptionToDto(exception);
    }

    /**
     * Обрабатывает исключения типа {@link NoSuchElementException}.
     *
     * @param e исключение, которое было выброшено
     * @return объект {@link ResponseEntity}, содержащий {@link ExceptionDto} и HTTP статус 404
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDto> handleNotFoundException(Exception e) {
        ExceptionDto dto = exceptionMapper.exceptionToDto(e);
        return ResponseEntity.status(404).body(dto);
    }
}
