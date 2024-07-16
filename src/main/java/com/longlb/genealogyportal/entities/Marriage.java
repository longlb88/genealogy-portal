package com.longlb.genealogyportal.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "marriage")
public class Marriage implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "spouse_1_id")
  private Person spouse1;

  @ManyToOne
  @JoinColumn(name = "spouse_2_id")
  private Person spouse2;

  @Column(name = "marriage_date")
  private LocalDate marriageDate;

  @Column(name = "marriage_date_type")
  private Integer marriageDateType;

  @Column(name = "is_divorced")
  private boolean isDivorced;

  @Column(name = "divorced_date")
  private LocalDate divorcedDate;

  @Column(name = "divorced_date_type")
  private Integer divorcedDateType;
}