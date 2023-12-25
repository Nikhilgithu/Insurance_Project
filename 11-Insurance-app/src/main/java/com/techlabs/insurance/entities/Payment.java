package com.techlabs.insurance.entities;

import java.time.LocalDate;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "payment")
@Getter
@Setter
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Payment {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "paymentid")
    private Long paymentId;
	@Column(name = "paymenttype")
	@Enumerated(EnumType.STRING)
    private PaymentType paymentType;
	@Column(name = "amount")
    private double amount;
	@Column(name = "date")
    private LocalDate date;
	@Column(name = "tax")
    private double tax;
	@Column(name = "totalpayment")
    private double totalPayment;
	
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name = "insurancepolicy_id")
	private InsurancePolicy insurancePolicy;
}
