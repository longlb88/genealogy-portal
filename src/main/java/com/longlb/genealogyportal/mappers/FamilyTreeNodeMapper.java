package com.longlb.genealogyportal.mappers;

import com.longlb.genealogyportal.dto.familytree.FamilyTreeNodeDTO;
import com.longlb.genealogyportal.dto.familytree.FamilyTreeSpouseNodeDTO;
import com.longlb.genealogyportal.entities.Marriage;
import com.longlb.genealogyportal.entities.Person;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = MarriageMapper.class
)
public interface FamilyTreeNodeMapper {
  @Mapping(target = "spouseNodes", expression = "java(new java.util.ArrayList<>())")
  @Mapping(target = "descendantNodes", expression = "java(new java.util.ArrayList<>())")
  FamilyTreeNodeDTO toDto(Person person);
  @Mapping(target = "marriageInfo", source = "marriage")
  @Mapping(target = "id", source = "person.id")
  FamilyTreeSpouseNodeDTO toSpouseDto(Person person, Marriage marriage);

}