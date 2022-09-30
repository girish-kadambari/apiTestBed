/*
 * *****************************************************************************
 *  Copyright (C) 2020 Testsigma Inc.
 *  All rights reserved.
 *  ****************************************************************************
 */

package com.testsigma.api_bed_test.dto;

import lombok.Data;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;

@Data
public class APIErrorDTO {
  private Instant timeStamp;
  private String error;
  private String code;
  private List<FieldErrorDTO> fieldErrors;
  private List<ObjectErrorDTO> objectErrors;

  @PostConstruct
  private void setTimeStamp() {
    this.timeStamp = Instant.now();
  }
}
