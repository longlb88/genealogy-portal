package com.longlb.genealogyportal.dto.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpouseDTO implements Serializable {
  private Long marriageId;
  private Long personId;
  private LocalDate marriageDate;
  private Integer marriageDateType;
  private boolean isDivorced;
  private LocalDate divorcedDate;
  private Integer divorcedDateType;
}
