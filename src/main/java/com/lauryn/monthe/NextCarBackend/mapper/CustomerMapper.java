package com.lauryn.monthe.NextCarBackend.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lauryn.monthe.NextCarBackend.domain.Customer;
import com.lauryn.monthe.NextCarBackend.domain.Gender;
import com.lauryn.monthe.nextcar.api.model.ApiCustomer;
import com.lauryn.monthe.nextcar.api.model.ApiCustomerRequest;
import com.lauryn.monthe.nextcar.api.model.ApiGender;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
    private final AddressMapper addressMapper;

    public Customer toCustomer(ApiCustomerRequest apiCustomerRequest) {
        if (apiCustomerRequest == null){
            return null;
        }
        return Customer.builder()
            .id(UUID.randomUUID().toString())
            .lastname(apiCustomerRequest.getLastname())
            .firstname(apiCustomerRequest.getFirstname())
            .gender(Gender.fromValue(apiCustomerRequest.getGender().getValue()))
            .birthday( apiCustomerRequest.getBirthday() != null ?  apiCustomerRequest.getBirthday() : null  )
            .address( addressMapper.toAddress(apiCustomerRequest.getAddress()))
            .phoneNumber(apiCustomerRequest.getPhoneNumber())
            .email(apiCustomerRequest.getEmail())
            .build();
    }

    public ApiCustomer toApiCustomer(Customer customer){
        if (customer== null){
            return null;
        }
        ApiCustomer apiCustomer = new ApiCustomer();
        return apiCustomer
            .id(customer.getId())
            .lastname(customer.getLastname())
            .firstname(customer.getFirstname())
            .gender(ApiGender.fromValue(customer.getGender().toString()))
            .birthday(customer.getBirthday() != null ? customer.getBirthday() : null)
            .address(addressMapper.toApiAddress(customer.getAddress()))
            .phoneNumber(customer.getPhoneNumber())
            .email(customer.getEmail());
    }

    public List <ApiCustomer> toApiCustomers(List<Customer> customers){
        if (customers == null){
            return null;
        }
        return  customers.stream()
            .map(this::toApiCustomer)
            .collect(Collectors.toList());
    }
}
