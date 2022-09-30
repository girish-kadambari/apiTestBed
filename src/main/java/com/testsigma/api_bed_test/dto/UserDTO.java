package com.testsigma.api_bed_test.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}

