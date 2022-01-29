package br.com.zup.comicsapi.handlers;

import java.util.ArrayList;
import java.util.List;

public class ApiErrorList {

    private Integer httpValue;
    private String httpError;
    private String message;
    private List<String> errors;

    public ApiErrorList(Integer httpValue, String httpError, String message, List<String> errors) {
        this.httpValue = httpValue;
        this.httpError = httpError;
        this.message = message;
        this.errors = errors;
    }

    public ApiErrorList(Integer httpValue, String httpError, String message, String error) {
        this.httpValue = httpValue;
        this.httpError = httpError;
        this.message = message;
        errors = new ArrayList<>(List.of(error));
    }

    public ApiErrorList(Integer httpValue, String httpError, String message) {
        this.httpValue = httpValue;
        this.httpError = httpError;
        this.message = message;
        errors = new ArrayList<>();
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

    public List<String> getErrors() {
        return errors;
    }

}
