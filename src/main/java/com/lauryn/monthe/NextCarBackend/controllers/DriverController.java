package com.lauryn.monthe.NextCarBackend.controllers;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lauryn.monthe.NextCarBackend.services.DriverService;
import com.lauryn.monthe.nextcar.api.DriverApi;
import com.lauryn.monthe.nextcar.api.model.ApiDriverId;
import com.lauryn.monthe.nextcar.api.model.ApiDriverRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class DriverController implements DriverApi{
    private final DriverService driverService;

    @Override
    public ResponseEntity<ApiDriverId> createDriver(ApiDriverRequest apiDriverRequest) {
        var driverId = driverService.createDriver(apiDriverRequest);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(driverId.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(driverId);
    }
}
