package com.longlb.genealogyportal.dto.person;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class BasePersonDTO implements Serializable {
  protected String firstName;
  protected String lastName;
  protected Integer gender;
  protected LocalDate dateOfBirth;
  protected Integer dateOfBirthType;
  protected LocalDate dateOfDeath;
  protected Integer dateOfDeathType;
  protected ParentRelationshipDTO parentRelationship;
  protected List<SpouseDTO> spouses;
}
