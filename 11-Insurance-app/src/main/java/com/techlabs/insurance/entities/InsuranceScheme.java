package com.techlabs.insurance.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "insurancescheme")
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class InsuranceScheme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int schemeId;

	@Column
	private String schemeName;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "insuranceplan_id")
	private InsurancePlan insurancePlan;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "detail_id")
	private SchemeDetails schemeDetails;

	@OneToMany(mappedBy = "insuranceScheme", cascade = CascadeType.ALL)
	private List<InsurancePolicy> policies;

	@Column(name = "status")
	private Status status;
}

