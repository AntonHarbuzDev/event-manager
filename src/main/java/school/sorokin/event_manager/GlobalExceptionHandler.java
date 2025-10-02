package school.sorokin.event_manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        log.debug("Handle no such element exception {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug("Handle method argument not valid exception {}", e.getMessage());
        String errorDetails = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        ErrorResponse errorResponse = new ErrorResponse(
                "Некорректный запрос",
                errorDetails,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
        log.error("Exception from server {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "Внутренняя ошибка сервера",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}