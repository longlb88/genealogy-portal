package com.longlb.genealogyportal.dto.familytree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.longlb.genealogyportal.dto.person.SpouseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FamilyTreeSpouseNodeDTO extends FamilyTreeNodeDTO {
  private SpouseDTO marriageInfo;
}
