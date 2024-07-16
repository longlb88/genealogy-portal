package com.longlb.genealogyportal.services;

import com.longlb.genealogyportal.dto.person.CreatePersonDTO;
import com.longlb.genealogyportal.dto.person.ResponsePersonDTO;
import com.longlb.genealogyportal.dto.person.UpdatePersonDTO;
import com.longlb.genealogyportal.entities.Person;
import org.apache.coyote.BadRequestException;

import java.util.Optional;

/*
  Methods declaration for interacting with person
 */
public interface PersonService {
  ResponsePersonDTO create(CreatePersonDTO dto) ;

  ResponsePersonDTO update(UpdatePersonDTO dto) ;

  Optional<Person> findById(Long personId);
}
