package com.lauryn.monthe.NextCarBackend.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lauryn.monthe.NextCarBackend.services.CustomerService;
import com.lauryn.monthe.nextcar.api.CustomerApi;
import com.lauryn.monthe.nextcar.api.model.ApiCustomer;
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

    @Override
    public ResponseEntity<List<ApiCustomer>> getCustomers() {
        return ResponseEntity.ok(customerService.getCustomers());
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateCustomer(String id, ApiCustomerRequest apiCustomerRequest) {
        customerService.updateCustomer(id, apiCustomerRequest);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ApiCustomer> getCustomerById(String id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Override
    public ResponseEntity<ApiCustomer> getCustomerByEmail(String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

}
