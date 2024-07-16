package com.longlb.genealogyportal.controllers;

import com.longlb.genealogyportal.dto.familytree.FamilyTreeNodeDTO;
import com.longlb.genealogyportal.services.FamilyTreeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/family-tree")
@AllArgsConstructor
public class FamilyTreeController {
  private FamilyTreeService familyTreeService;

  @GetMapping()
  public ResponseEntity<FamilyTreeNodeDTO> getFamilyTree(Long personId) {
    // Get current user family tree,
    // in reality with use current user attach in token, session, etc.
    // For this assignment, without authentication module, simulate using request param instead
    return new ResponseEntity<>(familyTreeService.getFullFamilyTree(personId), HttpStatus.OK);
  }

  @GetMapping("/{personId}")
  public ResponseEntity<FamilyTreeNodeDTO> getFamilyTreeWithParentAndDescendant(@PathVariable("personId") Long personId) {
    // Get current user family tree based on given user including parents and his/her descendants
    return new ResponseEntity<>(familyTreeService.getFamilyTreeWithParentAndDescendant(personId), HttpStatus.OK);
  }
}
