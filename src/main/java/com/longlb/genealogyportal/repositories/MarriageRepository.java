package com.longlb.genealogyportal.repositories;

import com.longlb.genealogyportal.entities.Marriage;
import com.longlb.genealogyportal.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarriageRepository extends JpaRepository<Marriage, Long> {
  @Query("""
    SELECT m
    FROM Marriage m
    WHERE m.spouse1 = :person OR m.spouse2 = :person
    ORDER BY m.marriageDate DESC
  """)
  List<Marriage> getMarriagesByPerson(Person person);

  @Query("""
    SELECT m
    FROM Marriage m
    WHERE (m.spouse1 = :person1 AND m.spouse2 = :person2)
    OR (m.spouse1 = :person2 AND m.spouse2 = :person1)
  """)
  Marriage getMarriageOf2Persons(Person person1, Person person2);
}