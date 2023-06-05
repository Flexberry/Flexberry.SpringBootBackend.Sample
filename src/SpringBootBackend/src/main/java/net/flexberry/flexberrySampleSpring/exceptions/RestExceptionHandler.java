package net.flexberry.flexberrySampleSpring.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import net.flexberry.flexberrySampleSpring.service.LoggingService;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {
    LoggingService loggingService = new LoggingService();

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleJsonMappingException(JsonMappingException ex) {
        loggingService.LogError(ex.getLocalizedMessage(), ex);

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleDefaultException(Throwable ex) {
        loggingService.LogError(ex.getLocalizedMessage(), ex);

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}