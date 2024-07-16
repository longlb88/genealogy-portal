package com.longlb.genealogyportal.services;

import com.longlb.genealogyportal.dto.familytree.FamilyTreeNodeDTO;

/*
  Methods declaration for interacting with family tree
 */
public interface FamilyTreeService {
  FamilyTreeNodeDTO getFullFamilyTree(Long personId);

  FamilyTreeNodeDTO getFamilyTreeWithParentAndDescendant(Long personId);
}
