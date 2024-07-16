package com.longlb.genealogyportal.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "parent_child_relation")
public class ParentChildRelation implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Person parent;

  @ManyToOne
  @JoinColumn(name = "child_id")
  private Person child;

  @Column(name = "parent_role")
  private Integer parentRole;

  @Column(name = "is_adopted")
  private boolean isAdopted;

  @Column(name = "adopted_date")
  private LocalDate adoptedDate;

  @Column(name = "adopted_date_type")
  private Integer adoptedDateType;
}