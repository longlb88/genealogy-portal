package com.longlb.genealogyportal.services.impl;

import com.longlb.genealogyportal.constants.ResponseMessage;
import com.longlb.genealogyportal.dto.person.ParentRelationshipDTO;
import com.longlb.genealogyportal.entities.ParentChildRelation;
import com.longlb.genealogyportal.entities.Person;
import com.longlb.genealogyportal.enums.ParentRoleEnum;
import com.longlb.genealogyportal.mappers.ParentChildRelationMapper;
import com.longlb.genealogyportal.repositories.ParentChildRelationRepository;
import com.longlb.genealogyportal.services.ParentChildRelationService;
import com.longlb.genealogyportal.services.PersonService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ParentChildRelationServiceImpl implements ParentChildRelationService {
  // REPOSITORIES
  private final ParentChildRelationRepository repository;

  // MAPPERS
  private final ParentChildRelationMapper mapper;

  // SERVICES
  private final PersonService personService;

  // CONSTRUCTORS
  public ParentChildRelationServiceImpl(ParentChildRelationRepository repository,
                                        ParentChildRelationMapper mapper,
                                        @Lazy PersonService personService
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.personService = personService;
  }

  @Override
  @Transactional
  public void saveParentChildRelationship(ParentRelationshipDTO dto, Person child) {
    if (dto != null) {
      if (dto.getFatherId() != null) {
        // Check if child has father
        ParentChildRelation fatherRelation = repository.findByChildIdAndParentRole(child.getId(), ParentRoleEnum.FATHER.getValue());
        if (fatherRelation != null) {
          if (!fatherRelation.getParent().getId().equals(dto.getFatherId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                ResponseMessage.ALREADY_HAS_FATHER, null);
          }
        }
        Optional<Person> fatherOptional = personService.findById(dto.getFatherId());
        saveParent(dto, child, fatherOptional, ParentRoleEnum.FATHER, fatherRelation);
      }
      if (dto.getMotherId() != null) {
        // Check if child has mother
        ParentChildRelation motherRelation = repository.findByChildIdAndParentRole(child.getId(), ParentRoleEnum.MOTHER.getValue());
        if (motherRelation != null) {
          if (!motherRelation.getParent().getId().equals(dto.getMotherId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                ResponseMessage.ALREADY_HAS_MOTHER, null);
          }
        }
        Optional<Person> motherOptional = personService.findById(dto.getMotherId());
        saveParent(dto, child, motherOptional, ParentRoleEnum.MOTHER, motherRelation);
      }
    }
  }

  @Override
  public List<ParentChildRelation> getParentRelationshipsByChild(Person child) {
    return repository.findByChildId(child.getId());
  }

  @Override
  public ParentChildRelation getParentRelationshipsByParentIdAndChildId(Long parentId, Long childId) {
    return repository.findByChildIdAndParentId(childId, parentId);
  }

  private void saveParent(ParentRelationshipDTO dto, Person child, Optional<Person> parentOptional, ParentRoleEnum parentRole, ParentChildRelation existedRelation) {
    if (existedRelation == null) {
      parentOptional.ifPresent(parent -> {
        ParentChildRelation relationship = mapper.toEntity(dto, child);
        relationship.setParent(parent);
        relationship.setParentRole(parentRole.getValue());
        repository.save(relationship);
      });
    } else {
      existedRelation = mapper.partialUpdate(dto, existedRelation);
      repository.save(existedRelation);
    }

  }
}
