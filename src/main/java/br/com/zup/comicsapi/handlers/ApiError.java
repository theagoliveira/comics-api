package br.com.zup.comicsapi.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class ApiError {

    private Integer httpValue;
    private String httpError;
    private String message;
    private Map<String, String> errors;

    public ApiError(Integer httpValue, String httpError, String message,
                    Map<String, String> errors) {
        this.httpValue = httpValue;
        this.httpError = httpError;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(Integer httpValue, String httpError, String message, String errorKey,
                    String errorValue) {
        this.httpValue = httpValue;
        this.httpError = httpError;
        this.message = message;
        errors = new HashMap<>(Map.of(errorKey, errorValue));
    }

    public Integer getHttpValue() {
        return httpValue;
    }

    public String getHttpError() {
        return httpError;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

}
