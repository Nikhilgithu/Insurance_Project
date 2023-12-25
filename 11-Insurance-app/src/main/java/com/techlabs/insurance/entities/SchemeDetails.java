package com.techlabs.insurance.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "schemedetails")
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class SchemeDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "detailid")
	private int detailId;

	@Lob
    @Column(name = "image",columnDefinition = "MEDIUMBLOB") 
    private byte[] image;

	@Column(name = "description",length=1000)
	private String description;

	@Column(name = "minamount")
	private double minAmount;

	@Column(name = "maxamount")
	private double maxAmount;

	@Column(name = "mininvestment")
	private int minInvestment;

	@Column(name = "maxinvestment")
	private int maxInvestment;

	@Column(name = "minage")
	private int minAge;

	@Column(name = "maxage")
	private int maxAge;

	@Column(name = "profitration")
	private double profitRatio;

	@Column(name = "registrationcommission")
	private double registrationCommission;

	@Column(name = "installmentcommission")
	private double installmentCommission;
}
