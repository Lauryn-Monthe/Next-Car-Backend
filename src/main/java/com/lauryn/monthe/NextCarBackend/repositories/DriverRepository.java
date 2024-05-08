package com.lauryn.monthe.NextCarBackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lauryn.monthe.NextCarBackend.domain.Driver;
import com.lauryn.monthe.NextCarBackend.domain.Status;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {

    List<Driver> findAllByStatus(Status status);
    Optional<Driver> findByEmail(String email);

}
