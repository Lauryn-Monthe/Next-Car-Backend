package com.lauryn.monthe.NextCarBackend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lauryn.monthe.NextCarBackend.domain.Customer;
import com.lauryn.monthe.NextCarBackend.domain.Gender;
import com.lauryn.monthe.NextCarBackend.exceptions.CustomerNotFound;
import com.lauryn.monthe.NextCarBackend.mapper.CustomerMapper;
import com.lauryn.monthe.NextCarBackend.repositories.CustomerRepository;
import com.lauryn.monthe.nextcar.api.model.ApiCustomer;
import com.lauryn.monthe.nextcar.api.model.ApiCustomerId;
import com.lauryn.monthe.nextcar.api.model.ApiCustomerRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public ApiCustomerId createCustomer(ApiCustomerRequest apiCustomerRequest) {
        var customer = customerRepository.save(customerMapper.toCustomer(apiCustomerRequest));
        return new ApiCustomerId().id(customer.getId());
    }

    public ApiCustomer getCustomerById(String id) {
        return customerMapper.toApiCustomer(findCustomerById(id));
    }

    public Customer findCustomerById(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFound(id));
    }

    public ApiCustomer updateCustomer(String id, ApiCustomerRequest apiCustomerRequest) {
        var customer = findCustomerById(id);
        customer.setFirstname(apiCustomerRequest.getFirstname());
        customer.setLastname(apiCustomerRequest.getLastname());
        customer.setBirthday(apiCustomerRequest.getBirthday());
        customer.setGender(Gender.fromValue(apiCustomerRequest.getGender().getValue()));
        customer.setEmail(apiCustomerRequest.getEmail());
        customer.setPhoneNumber(apiCustomerRequest.getPhoneNumber());
        customer.getAddress().setCity(apiCustomerRequest.getAddress().getCity());
        customer.getAddress().setPostalCode(apiCustomerRequest.getAddress().getPostalCode().toString());
        customer.getAddress().setStreetAndNumber(apiCustomerRequest.getAddress().getStreetAndNumber().toString());
        customer.getAddress().setCountry(apiCustomerRequest.getAddress().getCountry());
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toApiCustomer(updatedCustomer);
      }

      public List<ApiCustomer> getCustomers() {
        List<Customer> listOfCustomers = customerRepository.findAll();
        return customerMapper.toApiCustomers(listOfCustomers);
      }

      public void deleteCustomer(String id) {
        var customer = findCustomerById(id);
        customerRepository.delete(customer);
      }
}
