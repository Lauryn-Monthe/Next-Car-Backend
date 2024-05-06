package com.lauryn.monthe.NextCarBackend.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lauryn.monthe.NextCarBackend.domain.Driver;
import com.lauryn.monthe.NextCarBackend.domain.Gender;
import com.lauryn.monthe.nextcar.api.model.ApiDriver;
import com.lauryn.monthe.nextcar.api.model.ApiDriverRequest;
import com.lauryn.monthe.nextcar.api.model.ApiGender;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DriverMapper {
    private final AddressMapper addressMapper;

    public Driver toDriver(ApiDriverRequest apiDriverRequest) {
        if (apiDriverRequest == null){
            return null;
        }
        return Driver.builder()
            .id(UUID.randomUUID().toString())
            .lastname(apiDriverRequest.getLastname())
            .firstname(apiDriverRequest.getFirstname())
            .gender(Gender.fromValue(apiDriverRequest.getGender().getValue()))
            .birthday( apiDriverRequest.getBirthday() != null ?  apiDriverRequest.getBirthday() : null  )
            .address( addressMapper.toAddress(apiDriverRequest.getAddress()))
            .phoneNumber(apiDriverRequest.getPhoneNumber())
            .email(apiDriverRequest.getEmail())
            .build();
    }

    public ApiDriver toApiDriver(Driver driver){
        if (driver== null){
            return null;
        }
        ApiDriver apiDriver = new ApiDriver();
        return apiDriver
            .id(driver.getId())
            .lastname(driver.getLastname())
            .firstname(driver.getFirstname())
            .gender(ApiGender.fromValue(driver.getGender().toString()))
            .birthday(driver.getBirthday() != null ? driver.getBirthday() : null)
            .address(addressMapper.toApiAddress(driver.getAddress()))
            .phoneNumber(driver.getPhoneNumber())
            .email(driver.getEmail());
    }

    public List <ApiDriver> toApiDrivers(List<Driver> customers){
        if (customers == null){
            return null;
        }
        return  customers.stream()
            .map(this::toApiDriver)
            .collect(Collectors.toList());
    }
}
