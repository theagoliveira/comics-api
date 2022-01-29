package br.com.zup.comicsapi.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

        ApiErrorMap apiErrorMap = new ApiErrorMap(
            returnStatus.value(), returnStatus.getReasonPhrase(), message, errors
        );

        return handleExceptionInternal(ex, apiErrorMap, headers, returnStatus, request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        HttpStatus returnStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "An unexpected error occurred.";

        ApiErrorList apiErrorList = new ApiErrorList(
            returnStatus.value(), returnStatus.getReasonPhrase(), message
        );

        return handleExceptionInternal(ex, apiErrorList, new HttpHeaders(), returnStatus, request);
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

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        // TODO Auto-generated method stub
        return super.handleMissingPathVariable(ex, headers, status, request);
    }

}
