package kr.hhplus.be.server.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        log.error("[{}] IllegalStateException: {}", HttpStatus.BAD_REQUEST, e.getMessage(), e);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("[{}] IllegalArgumentException: {}", HttpStatus.BAD_REQUEST, e.getMessage(), e);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("[{}] Exception: {}", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
