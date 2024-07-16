package com.longlb.genealogyportal.mappers;

import com.longlb.genealogyportal.dto.person.SpouseDTO;
import com.longlb.genealogyportal.entities.Marriage;
import com.longlb.genealogyportal.repositories.PersonRepository;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = PersonRepository.class
)
public interface MarriageMapper {
  @Mapping(target = "spouse2", source = "spouseDTO.personId", qualifiedByName = "getById")
  @Mapping(target = "spouse1", source = "currentPerson", qualifiedByName = "getById")
  Marriage toEntity(SpouseDTO spouseDTO, Long currentPerson);

  SpouseDTO toSpouseDTO(Marriage marriage);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Marriage partialUpdate(SpouseDTO createPersonDTO, @MappingTarget Marriage marriage);
}