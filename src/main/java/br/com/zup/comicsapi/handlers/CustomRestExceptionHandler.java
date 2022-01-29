package br.com.zup.comicsapi.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }

        HttpStatus returnStatus = HttpStatus.BAD_REQUEST;

        String errorWord = errors.size() == 1 ? "error" : "errors";
        String message = "Validation failed with " + errors.size() + " " + errorWord + ".";

        ApiError apiError = new ApiError(
            returnStatus.value(), returnStatus.getReasonPhrase(), message, errors
        );

        return handleExceptionInternal(ex, apiError, headers, returnStatus, request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        HttpStatus returnStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "An unexpected error occurred.";

        ApiError apiError = new ApiError(
            returnStatus.value(), returnStatus.getReasonPhrase(), message, null
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), returnStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        // TODO Auto-generated method stub
        return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        // TODO Auto-generated method stub
        return super.handleMissingPathVariable(ex, headers, status, request);
    }

}
