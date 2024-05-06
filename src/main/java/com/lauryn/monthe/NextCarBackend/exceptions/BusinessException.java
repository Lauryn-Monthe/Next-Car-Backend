package com.lauryn.monthe.NextCarBackend.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

    public abstract HttpStatus status();

}
