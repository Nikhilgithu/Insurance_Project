package com.techlabs.insurance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.insurance.entities.Agent;
import com.techlabs.insurance.entities.Customer;
import com.techlabs.insurance.entities.InsurancePolicy;
import com.techlabs.insurance.entities.Status;



public interface InsurencePolicyRepository extends JpaRepository<InsurancePolicy,Integer>{
	Page<InsurancePolicy> findByAgent(Agent agent, Pageable pageable);
	Page<InsurancePolicy> findByStatus(Status status, Pageable pageable);
	InsurancePolicy findBypolicyNo(int policyNumber);
	Page<InsurancePolicy> findByCustomerCustomerId(int customerid, Pageable pageable);
	 Page<InsurancePolicy> findByCustomerCustomerIdAndIssueDateBetween(
	            int customerId, LocalDate startDate, LocalDate endDate, Pageable pageable);
	List<InsurancePolicy> findByCustomer(Customer customer);
	
}
