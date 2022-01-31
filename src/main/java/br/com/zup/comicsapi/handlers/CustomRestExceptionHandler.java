package br.com.zup.comicsapi.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.zup.comicsapi.exceptions.InvalidIsbnException;
import br.com.zup.comicsapi.exceptions.InvalidUserIdException;
import feign.FeignException;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        HttpStatus returnStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "An unexpected error occurred.";

        ApiErrorList apiErrorList = new ApiErrorList(
            returnStatus.value(), returnStatus.getReasonPhrase(), message
        );

        return handleExceptionInternal(ex, apiErrorList, new HttpHeaders(), returnStatus, request);
    }

    @ExceptionHandler({FeignException.class})
    public ResponseEntity<Object> handleFeign(FeignException ex, WebRequest request) {
        HttpStatus returnStatus = HttpStatus.valueOf(ex.status());
        String message = "Error when retrieving comic data from Marvel.";
        Map<String, Object> errors;
        String error;

        try {
            errors = new ObjectMapper().readValue(ex.contentUTF8(), HashMap.class);
            error = (String) errors.getOrDefault("status", "");
        } catch (JsonProcessingException e) {
            error = "";
        }

        ApiErrorList apiErrorList = new ApiErrorList(
            returnStatus.value(), returnStatus.getReasonPhrase(), message, error
        );

        return handleExceptionInternal(ex, apiErrorList, new HttpHeaders(), returnStatus, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }

        HttpStatus returnStatus = HttpStatus.BAD_REQUEST;

        String errorWord = errors.size() == 1 ? "error" : "errors";
        String message = "Validation failed with " + errors.size() + " " + errorWord + ".";

        ApiErrorMap apiErrorMap = new ApiErrorMap(
            returnStatus.value(), returnStatus.getReasonPhrase(), message, errors
        );

        return handleExceptionInternal(ex, apiErrorMap, headers, returnStatus, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        String supportedMethodsStr = "";

        if (supportedMethods != null) {
            supportedMethodsStr = supportedMethods.stream()
                                                  .map(Object::toString)
                                                  .collect(Collectors.joining(", "));
        }

        String message = ex.getMethod() + " method is not supported. Supported methods: "
                + supportedMethodsStr;

        HttpStatus returnStatus = HttpStatus.METHOD_NOT_ALLOWED;

        ApiErrorList apiErrorList = new ApiErrorList(
            returnStatus.value(), returnStatus.getReasonPhrase(), ex.getLocalizedMessage(), message
        );

        return handleExceptionInternal(ex, apiErrorList, new HttpHeaders(), returnStatus, request);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex,
                                                            WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        HttpStatus returnStatus = HttpStatus.BAD_REQUEST;

        ApiErrorMap apiErrorMap = new ApiErrorMap(
            returnStatus.value(), returnStatus.getReasonPhrase(), ex.getLocalizedMessage(), errors
        );

        return handleExceptionInternal(ex, apiErrorMap, new HttpHeaders(), returnStatus, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        String message = "Required request parameter is not present";
        String error = ex.getParameterName() + " parameter is missing";
        HttpStatus returnStatus = HttpStatus.BAD_REQUEST;

        ApiErrorList apiErrorList = new ApiErrorList(
            returnStatus.value(), returnStatus.getReasonPhrase(), message, error
        );

        return handleExceptionInternal(ex, apiErrorList, new HttpHeaders(), returnStatus, request);
    }

    @ExceptionHandler({InvalidUserIdException.class})
    public ResponseEntity<Object> handleInvalidUserIdException(InvalidUserIdException ex,
                                                               WebRequest request) {
        HttpStatus returnStatus = HttpStatus.BAD_REQUEST;

        ApiErrorList apiErrorList = new ApiErrorList(
            returnStatus.value(), returnStatus.getReasonPhrase(), ex.getMessage(),
            "The provided userId is invalid."
        );

        return handleExceptionInternal(ex, apiErrorList, new HttpHeaders(), returnStatus, request);
    }

    @ExceptionHandler({InvalidIsbnException.class})
    public ResponseEntity<Object> handleInvalidIsbnException(InvalidIsbnException ex,
                                                             WebRequest request) {
        HttpStatus returnStatus = HttpStatus.BAD_REQUEST;

        ApiErrorList apiErrorList = new ApiErrorList(
            returnStatus.value(), returnStatus.getReasonPhrase(), ex.getMessage(),
            "The provided ISBN is invalid."
        );

        return handleExceptionInternal(ex, apiErrorList, new HttpHeaders(), returnStatus, request);
    }

}
