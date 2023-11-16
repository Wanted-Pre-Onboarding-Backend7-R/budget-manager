package server.preonboarding.budgetmanager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static server.preonboarding.budgetmanager.exception.CommonErrorResponse.makeErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        ErrorCode errorCode;
        try {
            FieldError firstFieldError = Objects.requireNonNull(e.getBindingResult().getFieldError());
            String errorCodeName = firstFieldError.getDefaultMessage();
            errorCode = ErrorCode.valueOf(errorCodeName);
        } catch (NullPointerException npe) {
            errorCode = ErrorCode.ERROR_CODE_NOT_ASSIGNED;
        } catch (IllegalArgumentException iae) {
            errorCode = ErrorCode.ERROR_CODE_NOT_FOUND;
        }
        return makeErrorResponse(errorCode);
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(CustomException e) {
        return makeErrorResponse(e.getErrorCode());
    }

}
