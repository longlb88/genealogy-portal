package com.longlb.genealogyportal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateTypeEnum {
  FULL_DATE(1),
  MONTH_AND_YEAR(2),
  YEAR_ONLY(3);

  private final int value;
}
