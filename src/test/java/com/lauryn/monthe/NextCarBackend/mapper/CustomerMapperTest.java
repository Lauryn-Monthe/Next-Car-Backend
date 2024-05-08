package com.lauryn.monthe.NextCarBackend.mapper;

import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.lauryn.monthe.NextCarBackend.domain.Address;
import com.lauryn.monthe.NextCarBackend.domain.Customer;
import com.lauryn.monthe.NextCarBackend.domain.Gender;
import com.lauryn.monthe.nextcar.api.model.ApiAddress;
import com.lauryn.monthe.nextcar.api.model.ApiCustomer;
import com.lauryn.monthe.nextcar.api.model.ApiCustomerRequest;
import com.lauryn.monthe.nextcar.api.model.ApiGender;

public class CustomerMapperTest {

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private CustomerMapper uut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    ApiAddress apiAddress = new ApiAddress()
        .city("ad2")
        .country("ad1")
        .postalCode("ad3")
        .streetAndNumber("ad4");
    Address address = new Address("ad4", "ad3", "ad2", "ad1");

    @Test
    public void shouldMapToCustomer() {
        Mockito.when(addressMapper.toAddress(apiAddress)).thenReturn(address);
        Mockito.when(addressMapper.toApiAddress(address)).thenReturn(apiAddress);

        ApiCustomerRequest apiCustomerRequest = new ApiCustomerRequest()
            .lastname("lastname")
            .firstname("firstname")
            .phoneNumber("phoneNumber")
            .email("customer@parcaune.com")
            .address(addressMapper.toApiAddress(address))
            .gender(ApiGender.fromValue(Gender.MR.getValue()))
            .birthday(LocalDate.of(2016, 8, 9));

        Customer expected = Customer.builder()
            .lastname("lastname")
            .firstname("firstname")
            .gender(Gender.fromValue(Gender.MR.getValue()))
            .birthday(LocalDate.of(2016, 8, 9))
            .address(addressMapper.toAddress(apiAddress))
            .phoneNumber("phoneNumber")
            .email("customer@parcaune.com")
            .build();

        Customer actual = uut.toCustomer(apiCustomerRequest);
        Mockito.verify(addressMapper, times(1)).toApiAddress(address);
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @Test
    public void shouldMapToApiCustomer() {
        Mockito.when(addressMapper.toAddress(apiAddress)).thenReturn(address);
        Mockito.when(addressMapper.toApiAddress(address)).thenReturn(apiAddress);

        Customer customer = Customer.builder()
            .id("50")
            .lastname("lastname")
            .firstname("firstname")
            .gender(Gender.fromValue(Gender.MR.getValue()))
            .birthday(LocalDate.of(2016, 8, 9))
            .address(addressMapper.toAddress(apiAddress))
            .phoneNumber("phoneNumber")
            .email("customer@parcaune.com")
            .build();

        ApiCustomer expected = new ApiCustomer()
            .id("50")
            .lastname("lastname")
            .firstname("firstname")
            .gender(ApiGender.fromValue(Gender.MR.getValue()))
            .birthday(LocalDate.of(2016, 8, 9))
            .address(new ApiAddress().country("ad1").city("ad2").postalCode( "ad3").streetAndNumber("ad4"))
            .phoneNumber("phoneNumber")
            .email("customer@parcaune.com");

        ApiCustomer actual = uut.toApiCustomer(customer);
        Mockito.verify(addressMapper, times(1)).toAddress(apiAddress);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMapToApiCustomers(){
        Mockito.when(addressMapper.toAddress(apiAddress)).thenReturn(address);
        Mockito.when(addressMapper.toApiAddress(address)).thenReturn(apiAddress);

        List<Customer> customers = List.of(
            Customer.builder().id(UUID.randomUUID().toString())
                .address(addressMapper.toAddress(apiAddress))
                .gender(Gender.MS)
                .birthday(LocalDate.now()).build(),
            Customer.builder().id(UUID.randomUUID().toString())
                .address(addressMapper.toAddress(apiAddress))
                .gender(Gender.MR)
                .birthday(LocalDate.now()).build(),
            Customer.builder().id(UUID.randomUUID().toString())
                .address(addressMapper.toAddress(apiAddress))
                .gender(Gender.MS)
                .birthday(LocalDate.now()).build()
        );
        List<ApiCustomer> expected = customers.stream().map(customer -> new ApiCustomer()
            .id(customer.getId())
                .address(apiAddress)
                .gender(ApiGender.fromValue(customer.getGender().toString()))
                .birthday(customer.getBirthday())
            ).collect(Collectors.toList());

        List<ApiCustomer> actual = uut.toApiCustomers(customers);
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(addressMapper, times(3)).toAddress(apiAddress);
    }

}
