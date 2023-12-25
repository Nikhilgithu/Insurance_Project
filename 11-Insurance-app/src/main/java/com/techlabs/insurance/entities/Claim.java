package com.techlabs.insurance.entities;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "claim")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Claim {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "claimid")
    private int claimId;
	@Column(name = "claimamount")
    private Double claimAmount;
	@Column(name = "bankaccountnumber")
    private String bankAccountNumber;
	@Column(name = "bankifsccode")
    private String bankIfscCode;
	@Column(name = "date")
    private LocalDate date;
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
    private ClaimStatus status;
	
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name="insurancepolicy_id")
	private InsurancePolicy insurancePolicy;
	
	@OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
	private List<Document> documents;
}

