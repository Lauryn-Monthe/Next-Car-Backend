package com.lauryn.monthe.NextCarBackend.exceptions;


import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class CustomerNotFound extends BusinessException {
    private final String id;

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return String.format("Customer with %s not found", id);
    }

}
