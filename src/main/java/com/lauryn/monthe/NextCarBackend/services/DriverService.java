package com.lauryn.monthe.NextCarBackend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lauryn.monthe.NextCarBackend.domain.Driver;
import com.lauryn.monthe.NextCarBackend.domain.Gender;
import com.lauryn.monthe.NextCarBackend.domain.Status;
import com.lauryn.monthe.NextCarBackend.exceptions.DriverNotFound;
import com.lauryn.monthe.NextCarBackend.mapper.DriverMapper;
import com.lauryn.monthe.NextCarBackend.repositories.DriverRepository;
import com.lauryn.monthe.nextcar.api.model.ApiDriver;
import com.lauryn.monthe.nextcar.api.model.ApiDriverId;
import com.lauryn.monthe.nextcar.api.model.ApiDriverRequest;
import com.lauryn.monthe.nextcar.api.model.ApiStatus;

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

    public ApiDriver updateDriver(String id, ApiDriverRequest apiDriverRequest) {
        var driver = findDriverById(id);
        driver.setFirstname(apiDriverRequest.getFirstname());
        driver.setLastname(apiDriverRequest.getLastname());
        driver.setBirthday(apiDriverRequest.getBirthday());
        driver.setGender(Gender.fromValue(apiDriverRequest.getGender().getValue()));
        driver.setEmail(apiDriverRequest.getEmail());
        driver.setPhoneNumber(apiDriverRequest.getPhoneNumber());
        driver.getAddress().setCity(apiDriverRequest.getAddress().getCity());
        driver.getAddress().setPostalCode(apiDriverRequest.getAddress().getPostalCode().toString());
        driver.getAddress().setStreetAndNumber(apiDriverRequest.getAddress().getStreetAndNumber().toString());
        driver.getAddress().setCountry(apiDriverRequest.getAddress().getCountry());
        driver.setPassword(apiDriverRequest.getPassword());
        Driver updatedDriver = driverRepository.save(driver);
        return driverMapper.toApiDriver(updatedDriver);
      }

      public List<ApiDriver> getDrivers() {
        List<Driver> listOfCustomers = driverRepository.findAll();
        return driverMapper.toApiDrivers(listOfCustomers);
      }

      public void deleteDriver(String id) {
        var customer = findDriverById(id);
        driverRepository.delete(customer);
      }

      public void updateDriverStatus(String id, String status) {
        if (status == null) {
            throw new RuntimeException("status can not be null");
        }
        var driver = driverRepository.findById(id).orElseThrow(() -> new DriverNotFound(id));
        driver.setStatus(Status.fromValue(status));
        driverRepository.save(driver);
    }

    public List<ApiDriver> getActiveDrivers() {
      List<Driver> listOfDrivers = driverRepository.findAllByStatus(Status.ACTIVE);
      return driverMapper.toApiDrivers(listOfDrivers);
    }

    public List<ApiDriver> getDriversByStatus(ApiStatus status) {
      List<Driver> listOfDrivers = driverRepository.findAllByStatus(Status.fromValue(status.getValue()));
      return driverMapper.toApiDrivers(listOfDrivers);
    }

    public ApiDriver getDriverByEmail(String email){
      var driver = driverRepository.findByEmail(email).orElseThrow(() -> new DriverNotFound(email));
      return driverMapper.toApiDriver(driver);
    }
}
