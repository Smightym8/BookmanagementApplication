package at.fhv.msp.bookmanagementapplication.view;

import at.fhv.msp.bookmanagementapplication.application.api.exception.*;
import at.fhv.msp.bookmanagementapplication.application.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleBookNotFoundException(BookNotFoundException ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .withStatusCode(HttpStatus.NOT_FOUND.value())
                .withMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(IsbnAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleIsbnAlreadyExistsException(IsbnAlreadyExistsException ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .withStatusCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorNotFoundException(AuthorNotFoundException ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .withStatusCode(HttpStatus.NOT_FOUND.value())
                .withMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(InvalidBookCreationException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidBookCreationException(InvalidBookCreationException ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .withStatusCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(InvalidBookUpdateException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidBookUpdateException(InvalidBookUpdateException ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .withStatusCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .withMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }
}
