package com.longlb.genealogyportal.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParentRoleEnum {
  FATHER(1, "Father"),
  MOTHER(2, "Mother");

  private final int value;
  private final String name;
}
