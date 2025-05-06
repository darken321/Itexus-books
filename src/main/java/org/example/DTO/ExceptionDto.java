package org.example.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * DTO для отображения в запросе информации об ошибке
 */
@Data
@Builder
public class ExceptionDto {
    String message;
    UUID uuid;
    ZonedDateTime exceptionServerTime;
    String type;
}
