package com.lauryn.monthe.NextCarBackend.services;

import org.springframework.stereotype.Service;

import com.lauryn.monthe.NextCarBackend.domain.Driver;
import com.lauryn.monthe.NextCarBackend.domain.Gender;
import com.lauryn.monthe.NextCarBackend.exceptions.DriverNotFound;
import com.lauryn.monthe.NextCarBackend.mapper.DriverMapper;
import com.lauryn.monthe.NextCarBackend.repositories.DriverRepository;
import com.lauryn.monthe.nextcar.api.model.ApiDriver;
import com.lauryn.monthe.nextcar.api.model.ApiDriverId;
import com.lauryn.monthe.nextcar.api.model.ApiDriverRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public ApiDriverId createDriver(ApiDriverRequest apiDriverRequest) {
        var driver = driverRepository.save(driverMapper.toDriver(apiDriverRequest));
        return new ApiDriverId().id(driver.getId());
    }

    public ApiDriver getDriverById(String id) {
        return driverMapper.toApiDriver(findDriverById(id));
    }

    public Driver findDriverById(String id) {
        return driverRepository.findById(id).orElseThrow(() -> new DriverNotFound(id));
    }

    public ApiDriver updateCustomer(String id, ApiDriverRequest apiCustomerRequest) {
        var driver = findDriverById(id);
        driver.setFirstname(apiCustomerRequest.getFirstname());
        driver.setLastname(apiCustomerRequest.getLastname());
        driver.setBirthday(apiCustomerRequest.getBirthday());
        driver.setGender(Gender.fromValue(apiCustomerRequest.getGender().getValue()));
        driver.setEmail(apiCustomerRequest.getEmail());
        driver.setPhoneNumber(apiCustomerRequest.getPhoneNumber());
        driver.getAddress().setCity(apiCustomerRequest.getAddress().getCity());
        driver.getAddress().setPostalCode(apiCustomerRequest.getAddress().getPostalCode().toString());
        driver.getAddress().setStreetAndNumber(apiCustomerRequest.getAddress().getStreetAndNumber().toString());
        driver.getAddress().setCountry(apiCustomerRequest.getAddress().getCountry());
        Driver updatedDriver = driverRepository.save(driver);
        return driverMapper.toApiDriver(updatedDriver);
      }
}
