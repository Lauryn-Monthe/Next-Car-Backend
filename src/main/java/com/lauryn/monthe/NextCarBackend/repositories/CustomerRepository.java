package com.lauryn.monthe.NextCarBackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lauryn.monthe.NextCarBackend.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);
}
