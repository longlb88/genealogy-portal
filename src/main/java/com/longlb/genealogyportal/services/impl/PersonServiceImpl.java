package com.longlb.genealogyportal.services.impl;

import com.longlb.genealogyportal.constants.RedisKeyPrefix;
import com.longlb.genealogyportal.constants.ResponseMessage;
import com.longlb.genealogyportal.dto.person.BasePersonDTO;
import com.longlb.genealogyportal.dto.person.CreatePersonDTO;
import com.longlb.genealogyportal.dto.person.ResponsePersonDTO;
import com.longlb.genealogyportal.dto.person.UpdatePersonDTO;
import com.longlb.genealogyportal.entities.Marriage;
import com.longlb.genealogyportal.entities.ParentChildRelation;
import com.longlb.genealogyportal.entities.Person;
import com.longlb.genealogyportal.mappers.PersonMapper;
import com.longlb.genealogyportal.repositories.PersonRepository;
import com.longlb.genealogyportal.services.MarriageService;
import com.longlb.genealogyportal.services.ParentChildRelationService;
import com.longlb.genealogyportal.services.PersonService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
  // REPOSITORIES
  private final PersonRepository repository;

  // MAPPERS
  private final PersonMapper mapper;

  // SERVICES
  private final MarriageService marriageService;
  private final ParentChildRelationService parentChildRelationService;

  // REDIS TEMPLATE
  private final RedisTemplate<String, Object> redisTemplate;

  // CONSTRUCTOR
  public PersonServiceImpl(PersonRepository repository,
                           PersonMapper mapper,
                           @Lazy MarriageService marriageService,
                           @Lazy ParentChildRelationService parentChildRelationService,
                           RedisTemplate<String, Object> redisTemplate
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.marriageService = marriageService;
    this.parentChildRelationService = parentChildRelationService;
    this.redisTemplate = redisTemplate;
  }

  @Override
  @Transactional
  public ResponsePersonDTO create(CreatePersonDTO dto) {
    Person person = mapper.toEntity(dto);
    repository.save(person);

    // Save parent and marriages relationships
    parentChildRelationService.saveParentChildRelationship(dto.getParentRelationship(), person);
    marriageService.saveMarriages(dto.getSpouses(), person);

    // Evict cache
    evictCache(dto, person);

    return mapper.toDto(person);
  }

  @Override
  @Transactional
  public ResponsePersonDTO update(UpdatePersonDTO dto) {
    Optional<Person> personOptional = repository.findById(dto.getId());
    if (personOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          ResponseMessage.NOT_FOUND_PERSON + dto.getId(), null);
    }
    Person person = personOptional.get();

    mapper.partialUpdate(dto, person);
    repository.save(person);

    // Save parent and marriages relationships
    parentChildRelationService.saveParentChildRelationship(dto.getParentRelationship(), person);
    marriageService.saveMarriages(dto.getSpouses(), person);

    // Evict cache
    evictCache(dto, person);

    return mapper.toDto(person);
  }

  @Override
  public Optional<Person> findById(Long personId) {
    return repository.findById(personId);
  }

  private void evictCache(BasePersonDTO dto, Person person) {
    // Evict CHILDREN_LIST of parents
    List<Long> parentIds = new ArrayList<>();
    if (dto.getParentRelationship() != null) {
      if (dto.getParentRelationship().getFatherId() != null) {
        parentIds.add(dto.getParentRelationship().getFatherId());
      }
      if (dto.getParentRelationship().getMotherId() != null) {
        parentIds.add(dto.getParentRelationship().getMotherId());
      }
    } else {
      List<ParentChildRelation> parentRelations = parentChildRelationService.getParentRelationshipsByChild(person);
      parentIds.addAll(parentRelations.stream().map(parentRelation -> parentRelation.getParent().getId()).toList());
    }
    parentIds.forEach(id -> redisTemplate.opsForValue().getAndDelete(RedisKeyPrefix.CHILDREN_LIST + id));

    // Evict MARRIAGE_LIST of spouses
    List<Long> spouseIds = new ArrayList<>();
    if (dto.getSpouses() != null) {
      spouseIds.addAll(dto.getSpouses()
          .stream()
          .map(spouseDTO -> person.getId())
          .toList());
    }
    List<Marriage> marriages = marriageService.getMarriagesByPerson(person);
    for (Marriage marriage : marriages) {
      Long spouseId = marriage.getSpouse1().getId().equals(person.getId())
          ? marriage.getSpouse2().getId()
          : marriage.getSpouse1().getId();
      spouseIds.add(spouseId);
    }

    spouseIds.forEach(id -> redisTemplate.opsForValue().getAndDelete(RedisKeyPrefix.MARRIAGE_LIST + id));
  }
}
