package com.lauryn.monthe.NextCarBackend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.lauryn.monthe.NextCarBackend.domain.Address;
import com.lauryn.monthe.NextCarBackend.domain.Customer;
import com.lauryn.monthe.NextCarBackend.domain.Gender;
import com.lauryn.monthe.NextCarBackend.exceptions.CustomerNotFound;
import com.lauryn.monthe.NextCarBackend.mapper.AddressMapper;
import com.lauryn.monthe.NextCarBackend.mapper.CustomerMapper;
import com.lauryn.monthe.NextCarBackend.repositories.CustomerRepository;
import com.lauryn.monthe.nextcar.api.model.ApiAddress;
import com.lauryn.monthe.nextcar.api.model.ApiCustomer;
import com.lauryn.monthe.nextcar.api.model.ApiCustomerId;
import com.lauryn.monthe.nextcar.api.model.ApiCustomerRequest;
import com.lauryn.monthe.nextcar.api.model.ApiGender;

public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private CustomerMapper customerMapper = new CustomerMapper(new AddressMapper());

    @InjectMocks
    private CustomerService uut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateCustomer() {
        var apiCustomerRequest = new ApiCustomerRequest();
        var expected = Customer.builder()
            .id(UUID.randomUUID().toString())
            .build();
        Mockito.doReturn(expected).when(customerMapper).toCustomer(apiCustomerRequest);
        Mockito.when(customerRepository.save(expected)).thenReturn(expected);

        ApiCustomerId actual = uut.createCustomer(apiCustomerRequest);

        Mockito.verify(customerRepository, Mockito.times(1)).save(expected);
        Mockito.verify(customerMapper, Mockito.times(1)).toCustomer(apiCustomerRequest);
        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    public void shouldGetCustomers() {
        List<Customer> customers = List.of(
            Customer.builder().id(UUID.randomUUID().toString()).build(),
            Customer.builder().id(UUID.randomUUID().toString()).build(),
            Customer.builder().id(UUID.randomUUID().toString()).build()
        );
        List<ApiCustomer> expected = customers.stream()
            .map(customer -> new ApiCustomer().id(customer.getId()))
            .collect(Collectors.toList());
        
        Mockito.when(customerRepository.findAll()).thenReturn(customers);
        Mockito.doReturn(expected).when(customerMapper).toApiCustomers(customers);

        List<ApiCustomer> actual = uut.getCustomers();

        Mockito.verify(customerMapper, Mockito.times(1)).toApiCustomers(customers);

        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldGetCustomerById(){
        var id = UUID.randomUUID().toString();
        Customer customer = Customer.builder().id(id).build();
        ApiCustomer expected = new ApiCustomer().id(customer.getId());

        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        Mockito.doReturn(expected).when(customerMapper).toApiCustomer(customer);

        ApiCustomer actual = uut.getCustomerById(id);

        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldGetCustomerByIdThrowsException() {
        var id = UUID.randomUUID().toString();
        Mockito.when(customerRepository.findById(id)).thenThrow(new CustomerNotFound(id));
        assertThrows(CustomerNotFound.class, () -> uut.getCustomerById(id));
    }

    @Test
    public void shouldDeleteCustomer(){
        var id = UUID.randomUUID().toString();
        var customer = Customer.builder()
            .id(id)
            .build();
        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.ofNullable(customer));
        Mockito.doNothing().when(customerRepository).delete(customer);
        uut.deleteCustomer(id);
        Mockito.verify(customerRepository).findById(anyString());
        Mockito.verify(customerRepository).delete(any(Customer.class));
    }

    @Test
    public void shouldUpdateCustomer(){
        var id = UUID.randomUUID().toString();
        ApiCustomerRequest apiCustomerRequest = new ApiCustomerRequest().firstname("first")
            .lastname("last").gender(ApiGender.MS).phoneNumber("098765")
            .address(new ApiAddress().city("Berlin").country("Germany"));

        var customer = Customer.builder()
            .id(id)
            .firstname("firstname")
            .lastname("lastname")
            .gender(Gender.MR)
            .phoneNumber("12345")
            .address(Address.builder().city("Erlangen").country("Germany").build())
            .build();
        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        Mockito.when(customerRepository.save(any(Customer.class))).thenAnswer(args -> args.getArgument(0, Customer.class));
        Mockito.when(customerMapper.toApiCustomer(any(Customer.class))).thenCallRealMethod();
        // When
        var actualCustomer = uut.updateCustomer(id, apiCustomerRequest);
        // Then
        assertEquals(apiCustomerRequest.getFirstname(), actualCustomer.getFirstname());
        assertEquals(apiCustomerRequest.getPhoneNumber(), actualCustomer.getPhoneNumber());
        Mockito.verify(customerRepository).findById(anyString());
        Mockito.verify(customerRepository).save(any(Customer.class));
    }
}
