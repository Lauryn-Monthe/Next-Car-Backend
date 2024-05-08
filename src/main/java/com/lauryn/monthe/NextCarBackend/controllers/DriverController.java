package com.lauryn.monthe.NextCarBackend.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lauryn.monthe.NextCarBackend.services.DriverService;
import com.lauryn.monthe.nextcar.api.DriverApi;
import com.lauryn.monthe.nextcar.api.model.ApiDriver;
import com.lauryn.monthe.nextcar.api.model.ApiDriverId;
import com.lauryn.monthe.nextcar.api.model.ApiDriverRequest;
import com.lauryn.monthe.nextcar.api.model.ApiStatus;

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

    @Override
    public ResponseEntity<List<ApiDriver>> getDrivers(Optional<ApiStatus> status) {
        return ResponseEntity.ok(driverService.getDrivers());
    }

    @Override
    public ResponseEntity<Void> deleteDriver(String id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateDriver(String id, ApiDriverRequest ApiDriverRequest) {
        driverService.updateDriver(id, ApiDriverRequest);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ApiDriver> getDriverById(String id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @Override
    public ResponseEntity<Void> updateDriverStatus(String id, String body) {
        driverService.updateDriverStatus(id, body);
        return ResponseEntity.noContent().build();
    }
}
