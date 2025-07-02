package com.devteria.identityservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

// When serialize this object to JSON, exclude fields with value of `null`
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> { // generic type
    private int code = 1000;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
