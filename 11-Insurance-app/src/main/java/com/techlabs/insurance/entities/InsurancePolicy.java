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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "insurancepolicy")
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@ToString
public class InsurancePolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "policyno")
	private int policyNo;
	@Column(name = "issuedate")
	private LocalDate issueDate;
	@Column(name = "maturitydate")
	private LocalDate maturityDate;
	@Column(name = "premiumtype")
	private String premiumType;
	@Column(name = "totalinstallment")
	private int totalinstallment;
	@Column(name = "investAmount")
	private Double investamount;
	@Column(name = "premiumamount")
	private Double premiumamount;
	@Column(name = "sumassured")
	private Double sumAssured;
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "insurancescheme_id")
	private InsuranceScheme insuranceScheme;

	@OneToMany(mappedBy = "insurancePolicy", cascade = CascadeType.ALL)
	private List<Payment> payments;

	@OneToMany(mappedBy = "insurancePolicy", cascade = CascadeType.ALL)
	private List<Nominee> nominees;

	@OneToMany(mappedBy = "insurancePolicy", cascade = CascadeType.ALL)
	private List<Claim> claims;

	@OneToMany(mappedBy = "insurancePolicy", cascade = CascadeType.ALL)
	private List<Document> documents;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "agent_id")
	private Agent agent;

	public void setAgent(Agent agent) {
		this.agent = agent;
		agent.getInsurancePolicies().add(this);
	}

}
