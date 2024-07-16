package com.longlb.genealogyportal.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "gender")
  private Integer gender;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "date_of_birth_type")
  private Integer dateOfBirthType;

  @Column(name = "date_of_death")
  private LocalDate dateOfDeath;

  @Column(name = "date_of_death_type")
  private Integer dateOfDeathType;

  @OneToMany(mappedBy = "parent")
  private List<ParentChildRelation> childRelations;

  @OneToMany(mappedBy = "child")
  private List<ParentChildRelation> parentRelations;
}