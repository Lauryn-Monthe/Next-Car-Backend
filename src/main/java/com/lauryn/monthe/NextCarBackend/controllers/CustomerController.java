package com.lauryn.monthe.NextCarBackend.controllers;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lauryn.monthe.NextCarBackend.services.CustomerService;
import com.lauryn.monthe.nextcar.api.CustomerApi;
import com.lauryn.monthe.nextcar.api.model.ApiCustomerId;
import com.lauryn.monthe.nextcar.api.model.ApiCustomerRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CustomerController implements CustomerApi{
    private final CustomerService customerService;

    @Override
    public ResponseEntity<ApiCustomerId> createCustomer(ApiCustomerRequest apiCustomerRequest) {
        var customerId = customerService.createCustomer(apiCustomerRequest);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(customerId.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(customerId );
    }

}
