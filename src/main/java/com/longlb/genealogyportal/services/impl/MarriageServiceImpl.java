package com.longlb.genealogyportal.services.impl;

import com.longlb.genealogyportal.dto.person.SpouseDTO;
import com.longlb.genealogyportal.entities.Marriage;
import com.longlb.genealogyportal.entities.Person;
import com.longlb.genealogyportal.mappers.MarriageMapper;
import com.longlb.genealogyportal.repositories.MarriageRepository;
import com.longlb.genealogyportal.services.MarriageService;
import com.longlb.genealogyportal.services.PersonService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarriageServiceImpl implements MarriageService {
  // REPOSITORIES
  private final MarriageRepository repository;

  // MAPPERS
  private final MarriageMapper mapper;

  // SERVICES
  private final PersonService personService;

  // CONSTRUCTORS
  public MarriageServiceImpl(MarriageRepository repository,
                             MarriageMapper mapper,
                             @Lazy PersonService personService
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.personService = personService;
  }

  @Override
  @Transactional
  public void saveMarriages(List<SpouseDTO> spouses, Person person) {
    if (spouses != null && !spouses.isEmpty()) {
      for (SpouseDTO spouseDTO : spouses) {
        if (spouseDTO.getMarriageId() != null) {
          // Update marriage
          Optional<Marriage> marriageOptional = repository.findById(spouseDTO.getMarriageId());
          marriageOptional.ifPresent(marriage -> {
            marriage = mapper.partialUpdate(spouseDTO, marriage);
            repository.save(marriage);
          });
        } else {
          // Create new marriage
          Optional<Person> spouseOptional = personService.findById(spouseDTO.getPersonId());
          spouseOptional.ifPresent(spouse -> {
            Marriage marriage = mapper.toEntity(spouseDTO, person.getId());
            repository.save(marriage);
          });
        }
      }
    }
  }

  @Override
  public List<Marriage> getMarriagesByPerson(Person person) {
    return repository.getMarriagesByPerson(person);
  }

}
