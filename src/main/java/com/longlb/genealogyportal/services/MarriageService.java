package com.longlb.genealogyportal.services;

import com.longlb.genealogyportal.dto.person.SpouseDTO;
import com.longlb.genealogyportal.entities.Marriage;
import com.longlb.genealogyportal.entities.Person;

import java.util.List;

public interface MarriageService {
  void saveMarriages(List<SpouseDTO> spouseDTOS, Person person);
  List<Marriage> getMarriagesByPerson(Person person);
}
