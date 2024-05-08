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
import com.lauryn.monthe.NextCarBackend.domain.Driver;
import com.lauryn.monthe.NextCarBackend.domain.Gender;
import com.lauryn.monthe.NextCarBackend.domain.Status;
import com.lauryn.monthe.nextcar.api.model.ApiAddress;
import com.lauryn.monthe.nextcar.api.model.ApiDriver;
import com.lauryn.monthe.nextcar.api.model.ApiDriverRequest;
import com.lauryn.monthe.nextcar.api.model.ApiGender;
import com.lauryn.monthe.nextcar.api.model.ApiStatus;

public class DriverMapperTest {

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private DriverMapper uut;

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
    public void shouldMapToDriver() {
        Mockito.when(addressMapper.toAddress(apiAddress)).thenReturn(address);
        Mockito.when(addressMapper.toApiAddress(address)).thenReturn(apiAddress);

        ApiDriverRequest apiDriverRequest = new ApiDriverRequest()
            .lastname("lastname")
            .firstname("firstname")
            .phoneNumber("phoneNumber")
            .email("driver@parcaune.com")
            .address(addressMapper.toApiAddress(address))
            .gender(ApiGender.fromValue(Gender.MR.getValue()))
            .carName("carName")
            .birthday(LocalDate.of(2016, 8, 9));

        Driver expected = Driver.builder()
            .lastname("lastname")
            .firstname("firstname")
            .gender(Gender.fromValue(Gender.MR.getValue()))
            .birthday(LocalDate.of(2016, 8, 9))
            .address(addressMapper.toAddress(apiAddress))
            .phoneNumber("phoneNumber")
            .status(Status.ACTIVE)
            .carName("carName")
            .email("driver@parcaune.com")
            .build();

        Driver actual = uut.toDriver(apiDriverRequest);
        Mockito.verify(addressMapper, times(1)).toApiAddress(address);
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @Test
    public void shouldMapToApiDriver() {
        Mockito.when(addressMapper.toAddress(apiAddress)).thenReturn(address);
        Mockito.when(addressMapper.toApiAddress(address)).thenReturn(apiAddress);

        Driver driver = Driver.builder()
            .id("50")
            .lastname("lastname")
            .firstname("firstname")
            .gender(Gender.fromValue(Gender.MR.getValue()))
            .birthday(LocalDate.of(2016, 8, 9))
            .address(addressMapper.toAddress(apiAddress))
            .phoneNumber("phoneNumber")
            .status(Status.ACTIVE)
            .carName("carName")
            .email("driver@parcaune.com")
            .build();

        ApiDriver expected = new ApiDriver()
            .id("50")
            .lastname("lastname")
            .firstname("firstname")
            .gender(ApiGender.fromValue(Gender.MR.getValue()))
            .status(ApiStatus.fromValue(Status.ACTIVE.getValue()))
            .birthday(LocalDate.of(2016, 8, 9))
            .address(new ApiAddress().country("ad1").city("ad2").postalCode( "ad3").streetAndNumber("ad4"))
            .phoneNumber("phoneNumber")
            .carName("carName")
            .email("driver@parcaune.com");

        ApiDriver actual = uut.toApiDriver(driver);
        Mockito.verify(addressMapper, times(1)).toAddress(apiAddress);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMapToApiDrivers(){
        Mockito.when(addressMapper.toAddress(apiAddress)).thenReturn(address);
        Mockito.when(addressMapper.toApiAddress(address)).thenReturn(apiAddress);

        List<Driver> drivers = List.of(
            Driver.builder().id(UUID.randomUUID().toString())
                .address(addressMapper.toAddress(apiAddress))
                .gender(Gender.MS)
                .status(Status.ACTIVE)
                .birthday(LocalDate.now()).build(),
            Driver.builder().id(UUID.randomUUID().toString())
                .address(addressMapper.toAddress(apiAddress))
                .status(Status.ACTIVE)
                .gender(Gender.MR)
                .birthday(LocalDate.now()).build(),
            Driver.builder().id(UUID.randomUUID().toString())
                .status(Status.BUSY)
                .address(addressMapper.toAddress(apiAddress))
                .gender(Gender.MS)
                .birthday(LocalDate.now()).build()
        );
        List<ApiDriver> expected = drivers.stream().map(driver -> new ApiDriver()
            .id(driver.getId())
                .address(apiAddress)
                .gender(ApiGender.fromValue(driver.getGender().toString()))
                .status(ApiStatus.fromValue(driver.getStatus().toString()))
                .birthday(driver.getBirthday())
            ).collect(Collectors.toList());

        List<ApiDriver> actual = uut.toApiDrivers(drivers);
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(addressMapper, times(3)).toAddress(apiAddress);
    }

}
