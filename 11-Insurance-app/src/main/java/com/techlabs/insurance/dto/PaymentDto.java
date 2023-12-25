package com.techlabs.insurance.dto;

import java.time.LocalDate;

import com.techlabs.insurance.entities.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentDto {
	private Long paymentId;
	private int policyId;
    private PaymentType paymentType;
    private double amount;
    private LocalDate date;
    private double tax;
    private double totalPayment;
}
