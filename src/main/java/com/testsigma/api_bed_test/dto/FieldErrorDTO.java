/*
 * *****************************************************************************
 *  Copyright (C) 2020 Testsigma Inc.
 *  All rights reserved.
 *  ****************************************************************************
 */

package com.testsigma.api_bed_test.dto;

import lombok.Data;

@Data
public class FieldErrorDTO {
  private String field;
  private Object rejectedValue;
  private String message;
  private Boolean bindingFailure;
  private String[] codes;
}
