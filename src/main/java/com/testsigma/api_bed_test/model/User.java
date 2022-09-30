package com.testsigma.api_bed_test.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@Log4j2
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;
}
