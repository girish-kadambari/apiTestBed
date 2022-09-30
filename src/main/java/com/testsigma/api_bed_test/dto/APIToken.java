package com.testsigma.api_bed_test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIToken {
    private String domain;
    private String userName;
    private String email;
}
