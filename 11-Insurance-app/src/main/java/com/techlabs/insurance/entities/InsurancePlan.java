package com.techlabs.insurance.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "insuranceplan")
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class InsurancePlan {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planid")
    private int planId;

    @Column(name = "planname")
    private String planName;

    @OneToMany(mappedBy = "insurancePlan", cascade = CascadeType.ALL)
    private List<InsuranceScheme> schemes;

    @Column(name = "status")
    private Status status;
}
