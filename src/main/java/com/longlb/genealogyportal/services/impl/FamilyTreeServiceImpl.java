package com.longlb.genealogyportal.services.impl;

import com.longlb.genealogyportal.constants.RedisKeyPrefix;
import com.longlb.genealogyportal.constants.ResponseMessage;
import com.longlb.genealogyportal.dto.familytree.FamilyTreeNodeDTO;
import com.longlb.genealogyportal.dto.familytree.FamilyTreeSpouseNodeDTO;
import com.longlb.genealogyportal.entities.Marriage;
import com.longlb.genealogyportal.entities.ParentChildRelation;
import com.longlb.genealogyportal.entities.Person;
import com.longlb.genealogyportal.enums.ParentRoleEnum;
import com.longlb.genealogyportal.mappers.FamilyTreeNodeMapper;
import com.longlb.genealogyportal.mappers.ParentChildRelationMapper;
import com.longlb.genealogyportal.repositories.MarriageRepository;
import com.longlb.genealogyportal.repositories.PersonRepository;
import com.longlb.genealogyportal.services.FamilyTreeService;
import com.longlb.genealogyportal.services.ParentChildRelationService;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FamilyTreeServiceImpl implements FamilyTreeService {
  // REPOSITORIES
  private final PersonRepository personRepository;
  private final MarriageRepository marriageRepository;

  // SERVICES
  private final ParentChildRelationService parentChildRelationService;

  // MAPPERS
  private FamilyTreeNodeMapper familyTreeNodeMapper;
  private ParentChildRelationMapper parentChildRelationMapper;

  // CACHE TEMPLATE
  private RedisTemplate<String, Object> redisTemplate;

  @Override
  @Transactional(readOnly = true)
  public FamilyTreeNodeDTO getFullFamilyTree(Long personId) {
    Optional<Person> personOptional = personRepository.findById(personId);
    if (personOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          ResponseMessage.NOT_FOUND_PERSON + personId, null);
    }
    Person person = personOptional.get();

    // Get root based on paternal family
    Person father = personRepository.getParent(personId, ParentRoleEnum.FATHER.getValue());
    Person rootPerson = person;
    while (father != null) {
      rootPerson = father;
      father = personRepository.getParent(father.getId(), ParentRoleEnum.FATHER.getValue());
    }

    return buildTree(rootPerson, null);
  }

  @Override
  @Transactional(readOnly = true)
  public FamilyTreeNodeDTO getFamilyTreeWithParentAndDescendant(Long personId) {
    Optional<Person> personOptional = personRepository.findById(personId);
    if (personOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          ResponseMessage.NOT_FOUND_PERSON + personId, null);
    }
    Person person = personOptional.get();

    // Get parents
    Person father = personRepository.getParent(personId, ParentRoleEnum.FATHER.getValue());
    Person mother = personRepository.getParent(personId, ParentRoleEnum.MOTHER.getValue());

    // If it has no parent, person is root of tree
    if (father == null && mother == null) {
      return buildTree(person, null);
    }

    // If it has at least 1 parent
    FamilyTreeNodeDTO rootNode;
    if (father != null) { // Father as root if father is not null
      rootNode = familyTreeNodeMapper.toDto(father);

      if (mother != null) {
        Marriage marriage = marriageRepository.getMarriageOf2Persons(father, mother);
        FamilyTreeSpouseNodeDTO motherNode = familyTreeNodeMapper.toSpouseDto(mother, marriage);
        rootNode.getSpouseNodes().add(motherNode);
      }
    } else { // Mother as root if father null
      rootNode = familyTreeNodeMapper.toDto(mother);
    }
    ParentChildRelation parentChildRelation = parentChildRelationService.getParentRelationshipsByParentIdAndChildId(rootNode.getId(), person.getId());
    rootNode.getDescendantNodes().add(buildTree(person, parentChildRelation));

    return rootNode;
  }

  private FamilyTreeNodeDTO buildTree(Person rootPerson, ParentChildRelation parentChildRelation) {
    FamilyTreeNodeDTO rootNode = familyTreeNodeMapper.toDto(rootPerson);

    // Set parent relations
    if (parentChildRelation != null) {
      rootNode.setParentRelationship(parentChildRelationMapper.toDto(parentChildRelation));
      Person father = personRepository.getParent(rootPerson.getId(), ParentRoleEnum.FATHER.getValue());
      Person mother = personRepository.getParent(rootPerson.getId(), ParentRoleEnum.MOTHER.getValue());
      if (father != null) {
        rootNode.getParentRelationship().setFatherId(father.getId());
      }
      if (mother != null) {
        rootNode.getParentRelationship().setMotherId(mother.getId());
      }
    }

    // Add spouses to members list
    List<Marriage> marriageList = (List<Marriage>) redisTemplate.opsForValue().get(RedisKeyPrefix.MARRIAGE_LIST + rootPerson.getId());
    if (marriageList == null) {
      marriageList = marriageRepository.getMarriagesByPerson(rootPerson);
      if (marriageList != null) {
        redisTemplate.opsForValue().set(RedisKeyPrefix.MARRIAGE_LIST + rootPerson.getId(), marriageList, Duration.ofMinutes(60));
      }
    }
    if (marriageList != null && !marriageList.isEmpty()) {
      for (Marriage marriage : marriageList) {
        Person spouse = rootPerson.getId().equals(marriage.getSpouse2().getId())
            ? marriage.getSpouse1()
            : marriage.getSpouse2();

        FamilyTreeSpouseNodeDTO spouseNode = familyTreeNodeMapper.toSpouseDto(spouse, marriage);
        rootNode.getSpouseNodes().add(spouseNode);
      }
    }

    // Add children
    List<ParentChildRelation> childRelationList = (List<ParentChildRelation>) redisTemplate.opsForValue().get(RedisKeyPrefix.CHILDREN_LIST + rootPerson.getId());
    if (childRelationList == null) {
      Hibernate.initialize(rootPerson.getChildRelations());
      childRelationList = rootPerson.getChildRelations();
      childRelationList.sort((r1, r2) -> r1.getChild().getDateOfBirth().isAfter(r2.getChild().getDateOfBirth()) ? 1 : -1);
      redisTemplate.opsForValue().set(RedisKeyPrefix.CHILDREN_LIST + rootPerson.getId(), childRelationList, Duration.ofMinutes(60));
    }
    childRelationList
        .stream()
        .forEach(relation -> {
          FamilyTreeNodeDTO childMember = buildTree(relation.getChild(), relation);
          rootNode.getDescendantNodes().add(childMember);
        });

    return rootNode;
  }
}
