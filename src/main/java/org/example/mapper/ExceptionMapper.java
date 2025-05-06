package org.example.mapper;

import org.example.DTO.ExceptionDto;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 *  Класс для преобразования исключений в объекты {@link ExceptionDto}.
 */
@Component
public class ExceptionMapper {
    public ExceptionDto exceptionToDto(Exception ex) {
        return ExceptionDto.builder()
                .message(ex.getMessage())
                .uuid(UUID.randomUUID())
                .exceptionServerTime(ZonedDateTime.now())
                .type(ex.getClass().getSimpleName())
                .build();
    }
}
