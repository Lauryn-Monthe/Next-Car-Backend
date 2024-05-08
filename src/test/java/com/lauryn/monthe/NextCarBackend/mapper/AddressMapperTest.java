package com.lauryn.monthe.NextCarBackend.mapper;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.lauryn.monthe.NextCarBackend.domain.Address;
import com.lauryn.monthe.nextcar.api.model.ApiAddress;

public class AddressMapperTest {

    private final AddressMapper uut = new AddressMapper();
    
    @Test
    public void shouldMapAddress() {
        var input = new ApiAddress()
            .streetAndNumber("streetAndNumber")
            .postalCode("postalCode")
            .country("country")
            .city("location");

        var expected = Address.builder()
            .streetAndNumber("streetAndNumber")
            .postalCode("postalCode")
            .country("country")
            .city("location")
            .build();

        var actual = uut.toAddress(input);
        Assertions.assertThat(actual.getCountry()).isEqualTo(expected.getCountry());
        Assertions.assertThat(actual.getCity()).isEqualTo(expected.getCity());
        Assertions.assertThat(actual.getPostalCode()).isEqualTo(Optional.of(expected.getPostalCode()).toString());
        Assertions.assertThat(actual.getStreetAndNumber()).isEqualTo(Optional.of(expected.getStreetAndNumber()).toString());
    }

    @Test
    public void shouldMapApiAddress() {
        var input = Address.builder()
            .streetAndNumber("streetAndNumber")
            .postalCode("postalCode")
            .country("country")
            .city("location")
            .build();

        var expected = new ApiAddress()
            .streetAndNumber("streetAndNumber")
            .postalCode("postalCode")
            .country("country")
            .city("location");

        var actual = uut.toApiAddress(input);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}
