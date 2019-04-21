package com.nordgym.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Empty database")
public class EmptyDataBaseException extends NullPointerException{
    private int statusCode;
    private String message;

    public EmptyDataBaseException() {
        this.statusCode = 404;
    }

    public EmptyDataBaseException(String message) {
       this.message = message;
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
