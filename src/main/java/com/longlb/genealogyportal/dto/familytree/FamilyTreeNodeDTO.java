package com.longlb.genealogyportal.dto.familytree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.longlb.genealogyportal.dto.person.BasePersonDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FamilyTreeNodeDTO extends BasePersonDTO {
  private Long id;
  private List<FamilyTreeSpouseNodeDTO> spouseNodes;
  private List<FamilyTreeNodeDTO> descendantNodes;
}
