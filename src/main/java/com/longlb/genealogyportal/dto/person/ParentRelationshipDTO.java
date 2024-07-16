package com.longlb.genealogyportal.dto.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParentRelationshipDTO implements Serializable {
  private Long fatherId;
  private Long motherId;
  private Boolean isAdopted;
  private LocalDate adoptedDate;
  private Integer adoptedDateType;
}
