package com.longlb.genealogyportal.repositories;

import com.longlb.genealogyportal.entities.ParentChildRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentChildRelationRepository extends JpaRepository<ParentChildRelation, Long> {
  ParentChildRelation findByChildIdAndParentRole(Long childId, Integer parentRole);

  ParentChildRelation findByChildIdAndParentId(Long childId, Long parentId);

  List<ParentChildRelation> findByChildId(Long childId);
}