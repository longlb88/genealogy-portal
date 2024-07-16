package com.longlb.genealogyportal.mappers;

import com.longlb.genealogyportal.dto.person.CreatePersonDTO;
import com.longlb.genealogyportal.dto.person.ResponsePersonDTO;
import com.longlb.genealogyportal.dto.person.UpdatePersonDTO;
import com.longlb.genealogyportal.entities.Person;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonMapper {
  Person toEntity(ResponsePersonDTO responsePersonDTO);
  Person toEntity(CreatePersonDTO createPersonDTO);
  Person toEntity(UpdatePersonDTO updatePersonDTO);
  ResponsePersonDTO toDto(Person person);
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  Person partialUpdate(UpdatePersonDTO updatePersonDTO, @MappingTarget Person person);
}