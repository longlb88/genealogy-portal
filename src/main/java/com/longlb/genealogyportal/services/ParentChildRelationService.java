package com.longlb.genealogyportal.services;

import com.longlb.genealogyportal.dto.person.ParentRelationshipDTO;
import com.longlb.genealogyportal.entities.ParentChildRelation;
import com.longlb.genealogyportal.entities.Person;

import java.util.List;

public interface ParentChildRelationService {
  void saveParentChildRelationship(ParentRelationshipDTO dto, Person child);

  List<ParentChildRelation> getParentRelationshipsByChild(Person child);

  ParentChildRelation getParentRelationshipsByParentIdAndChildId(Long parentId, Long childId);
}
