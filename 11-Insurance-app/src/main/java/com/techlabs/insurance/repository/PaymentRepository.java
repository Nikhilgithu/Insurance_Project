package com.techlabs.insurance.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.insurance.entities.InsurancePolicy;
import com.techlabs.insurance.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	Page<Payment> findByInsurancePolicy(InsurancePolicy insurancePolicy, Pageable pageable);

	Page<Payment> findByInsurancePolicyAndDateBetween(InsurancePolicy insurancePolicy, LocalDate startDate,
			LocalDate endDate, Pageable pageable);
}
