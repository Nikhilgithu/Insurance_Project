package com.techlabs.insurance.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.techlabs.insurance.dto.PaymentDto;

public interface PaymentService {
	String addPayment(PaymentDto paymentDto);

	Page<PaymentDto> getPaymentsbyPolicyid(int policyid, int pageno, int pagesize);

	Page<PaymentDto> getPaymentsbyPolicyid(int policyid, LocalDate startDate, LocalDate endDate, int pageno,
			int pagesize);
}
