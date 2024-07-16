package com.longlb.genealogyportal.repositories;

import com.longlb.genealogyportal.entities.Person;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
  @Query("""
    SELECT p
    FROM Person p
    JOIN ParentChildRelation prc on p.id = prc.parent.id
    WHERE prc.child.id = :personId
    AND prc.parentRole = :parentRole
""")
  Person getParent(Long personId, Integer parentRole);

  @Named("getById")
  Person getById(Long id);
}