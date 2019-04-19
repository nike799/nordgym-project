package com.nordgym.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "This user doesn't exists")
public class ResourceNotFoundException extends NullPointerException {
    private int statusCode;

    public ResourceNotFoundException() {
        this.statusCode = 404;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
