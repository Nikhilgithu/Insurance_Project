package com.techlabs.insurance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.insurance.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	 Page<Customer> findByFirstnameContaining(String keyword, Pageable pageable);
	
	Page<Customer> findByPolicies_policyNoContainingAndFirstnameContainingOrLastnameContainingOrCityContainingOrMobileNoContainingOrEmailContainingOrStateContaining(
			int policyNo, String keyword, String keyword2, String keyword3, String keyword4,String keyword5,String keyword6, Pageable pageable);
	Page<Customer> findByFirstnameContainingOrMobileNoContainingOrEmailContainingOrLastnameContainingOrCityContainingOrStateContaining(String keyword,
			String keyword2, String keyword3, String keyword4,String keyword5,String keyword6, Pageable pageable);
}
