package com.longlb.genealogyportal.dto.person;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponsePersonDTO extends BasePersonDTO {
  private Long id;
}
