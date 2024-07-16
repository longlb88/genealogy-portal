package com.longlb.genealogyportal.controllers;

import com.longlb.genealogyportal.dto.person.CreatePersonDTO;
import com.longlb.genealogyportal.dto.person.ResponsePersonDTO;
import com.longlb.genealogyportal.dto.person.UpdatePersonDTO;
import com.longlb.genealogyportal.services.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
@AllArgsConstructor
public class PersonController {
  private PersonService personService;

  @PostMapping()
  public ResponseEntity<ResponsePersonDTO> createPerson(@RequestBody CreatePersonDTO dto) {
    return new ResponseEntity<>(personService.create(dto), HttpStatus.OK);
  }

  @PutMapping()
  public ResponseEntity<ResponsePersonDTO> updatePerson(@RequestBody UpdatePersonDTO dto) {
    return new ResponseEntity<>(personService.update(dto), HttpStatus.OK);
  }
}
