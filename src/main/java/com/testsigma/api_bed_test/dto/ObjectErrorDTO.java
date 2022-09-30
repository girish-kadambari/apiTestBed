/*
 * *****************************************************************************
 *  Copyright (C) 2020 Testsigma Inc.
 *  All rights reserved.
 *  ****************************************************************************
 */

package com.testsigma.api_bed_test.dto;

import lombok.Data;

@Data
public class ObjectErrorDTO {
  private String objectName;
  private Object source;
  private String defaultMessage;
  private String[] codes;
}
