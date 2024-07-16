package com.longlb.genealogyportal.mappers;

import com.longlb.genealogyportal.dto.person.ParentRelationshipDTO;
import com.longlb.genealogyportal.entities.ParentChildRelation;
import com.longlb.genealogyportal.entities.Person;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ParentChildRelationMapper {
  @Mapping(target = "child", source = "child")
  @Mapping(target = "adopted", source = "parentRelationshipDTO.isAdopted")
  @Mapping(target = "id", ignore = true)
  ParentChildRelation toEntity(ParentRelationshipDTO parentRelationshipDTO, Person child);

  @Mapping(target = "isAdopted", source = "adopted")
  ParentRelationshipDTO toDto(ParentChildRelation parentChildRelation);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ParentChildRelation partialUpdate(ParentRelationshipDTO parentRelationshipDTO, @MappingTarget ParentChildRelation parentChildRelation);
}