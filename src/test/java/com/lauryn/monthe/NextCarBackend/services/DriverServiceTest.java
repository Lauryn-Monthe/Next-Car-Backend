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
import com.lauryn.monthe.NextCarBackend.domain.Driver;
import com.lauryn.monthe.NextCarBackend.domain.Gender;
import com.lauryn.monthe.NextCarBackend.domain.Status;
import com.lauryn.monthe.NextCarBackend.exceptions.DriverNotFound;
import com.lauryn.monthe.NextCarBackend.mapper.AddressMapper;
import com.lauryn.monthe.NextCarBackend.mapper.DriverMapper;
import com.lauryn.monthe.NextCarBackend.repositories.DriverRepository;
import com.lauryn.monthe.nextcar.api.model.ApiAddress;
import com.lauryn.monthe.nextcar.api.model.ApiDriver;
import com.lauryn.monthe.nextcar.api.model.ApiDriverId;
import com.lauryn.monthe.nextcar.api.model.ApiDriverRequest;
import com.lauryn.monthe.nextcar.api.model.ApiGender;

public class DriverServiceTest {
    @Mock
    private DriverRepository driverRepository;

    @Spy
    private DriverMapper driverMapper = new DriverMapper(new AddressMapper());

    @InjectMocks
    private DriverService uut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateDriver() {
        var apiDriverRequest = new ApiDriverRequest();
        var expected = Driver.builder()
            .id(UUID.randomUUID().toString())
            .build();
        Mockito.doReturn(expected).when(driverMapper).toDriver(apiDriverRequest);
        Mockito.when(driverRepository.save(expected)).thenReturn(expected);

        ApiDriverId actual = uut.createDriver(apiDriverRequest);

        Mockito.verify(driverRepository, Mockito.times(1)).save(expected);
        Mockito.verify(driverMapper, Mockito.times(1)).toDriver(apiDriverRequest);
        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    public void shouldGetDrivers() {
        List<Driver> drivers = List.of(
            Driver.builder().id(UUID.randomUUID().toString()).status(Status.ACTIVE).build(),
            Driver.builder().id(UUID.randomUUID().toString()).status(Status.ACTIVE).build(),
            Driver.builder().id(UUID.randomUUID().toString()).status(Status.ACTIVE).build()
        );
        List<ApiDriver> expected = drivers.stream()
            .map(driver -> new ApiDriver().id(driver.getId()))
            .collect(Collectors.toList());

        Mockito.when(driverRepository.findAllByStatus(Status.ACTIVE)).thenReturn(drivers);
        Mockito.doReturn(expected).when(driverMapper).toApiDrivers(drivers);

        List<ApiDriver> actual = uut.getActiveDrivers();

        Mockito.verify(driverRepository, Mockito.times(1)).findAllByStatus(any());
        Mockito.verify(driverMapper, Mockito.times(1)).toApiDrivers(drivers);

        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldGetDriverById(){
        var id = UUID.randomUUID().toString();
        Driver driver = Driver.builder().id(id).build();
        ApiDriver expected = new ApiDriver().id(driver.getId());

        Mockito.when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
        Mockito.doReturn(expected).when(driverMapper).toApiDriver(driver);

        ApiDriver actual = uut.getDriverById(id);

        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldGetDriverByIdThrowsException() {
        var id = UUID.randomUUID().toString();
        Mockito.when(driverRepository.findById(id)).thenThrow(new DriverNotFound(id));
        assertThrows(DriverNotFound.class, () -> uut.getDriverById(id));
    }

    @Test
    public void shouldDeleteDriver(){
        var id = UUID.randomUUID().toString();
        var driver = Driver.builder()
            .id(id)
            .build();
        Mockito.when(driverRepository.findById(id)).thenReturn(Optional.ofNullable(driver));
        Mockito.doNothing().when(driverRepository).delete(driver);
        uut.deleteDriver(id);
        Mockito.verify(driverRepository).findById(anyString());
        Mockito.verify(driverRepository).delete(any(Driver.class));
    }

    @Test
    public void shouldUpdateDriverStatus(){
        var id = UUID.randomUUID().toString();
        Driver driver = Driver.builder().id(id).status(Status.ACTIVE).build();
        Mockito.when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
        uut.updateDriverStatus(id, Status.BUSY.toString());
        Mockito.verify(driverRepository).findById(anyString());
        assertEquals(driver.getStatus(), Status.BUSY);
    }

    @Test
    public void shouldUpdateDriver(){
        var id = UUID.randomUUID().toString();
        ApiDriverRequest apiDriverRequest = new ApiDriverRequest().firstname("first")
            .lastname("last").gender(ApiGender.MS).phoneNumber("098765")
            .address(new ApiAddress().city("Berlin").country("Germany"));

        var driver = Driver.builder()
            .id(id)
            .status(Status.ACTIVE)
            .firstname("firstname")
            .lastname("lastname")
            .gender(Gender.MR)
            .phoneNumber("12345")
            .address(Address.builder().city("Erlangen").country("Germany").build())
            .build();
        Mockito.when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
        Mockito.when(driverRepository.save(any(Driver.class))).thenAnswer(args -> args.getArgument(0, Driver.class));
        Mockito.when(driverMapper.toApiDriver(any(Driver.class))).thenCallRealMethod();
        // When
        var actualDriver = uut.updateDriver(id, apiDriverRequest);
        // Then
        assertEquals(apiDriverRequest.getFirstname(), actualDriver.getFirstname());
        assertEquals(apiDriverRequest.getPhoneNumber(), actualDriver.getPhoneNumber());
        Mockito.verify(driverRepository).findById(anyString());
        Mockito.verify(driverRepository).save(any(Driver.class));
    }
}
