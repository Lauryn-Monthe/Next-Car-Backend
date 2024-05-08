package com.lauryn.monthe.NextCarBackend.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "DRIVER")
public class Driver {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    private String id;

    @Column(name = "LASTNAME")
    private String lastname;

    @Column(name = "FIRSTNAME")
    private String firstname;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "BIRTHDAY")
    private LocalDate birthday;

    @Embedded
    private Address address;
    

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "EMAIL", unique = true, nullable = false)
    @Email(regexp = "^[_a-z0-9\\-]+(\\.[_a-z0-9\\-]+)*@[a-z0-9\\-]+(\\.[A-Za-z0-9\\-]+)*\\.[0-9a-z]{2,}$")
    private String email;

    /* @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "patient")
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>(); */
}
