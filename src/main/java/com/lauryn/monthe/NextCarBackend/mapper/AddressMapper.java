package com.lauryn.monthe.NextCarBackend.mapper;

import org.springframework.stereotype.Component;

import com.lauryn.monthe.nextcar.api.model.ApiAddress;
import com.lauryn.monthe.NextCarBackend.domain.Address;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressMapper {
    public Address toAddress(ApiAddress apiAddress) {
        if (apiAddress == null){
            return null;
        }
        return  Address.builder()
            .streetAndNumber(apiAddress.getStreetAndNumber().toString())
            .country(apiAddress.getCountry())
            .postalCode(apiAddress.getPostalCode().toString())
            .city(apiAddress.getCity())
            .build();
    }

    public ApiAddress toApiAddress(Address address){
        if (address == null){
            return null;
        }
        ApiAddress apiAddress = new ApiAddress();
        return apiAddress
            .streetAndNumber(address.getStreetAndNumber())
            .postalCode(address.getPostalCode())
            .city(address.getCity())
            .country(address.getCountry());
    }
}
