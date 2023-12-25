package com.techlabs.insurance.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techlabs.insurance.entities.Claim;
import com.techlabs.insurance.entities.ClaimStatus;

public interface ClaimRepository extends JpaRepository<Claim, Integer>{
	List<Claim> findByInsurancePolicy_PolicyNo(int policyNumber, Pageable pageable);
	@Query("SELECT c FROM Claim c WHERE c.insurancePolicy.policyNo = :policyNumber AND c.status = :status")
    Page<Claim> findByPolicyNumberAndStatus(@Param("policyNumber") int policyNumber, @Param("status") ClaimStatus status, Pageable pageable);
	Page<Claim> findByStatus(ClaimStatus status, Pageable pageable);
	 List<Claim> findByDateBetween(LocalDate startDate,LocalDate endDate);
	List<Claim> findByInsurancePolicy_PolicyNoAndDateBetween(int policyNumber, LocalDate startDate, LocalDate endDate,
			Pageable pageable);
}
