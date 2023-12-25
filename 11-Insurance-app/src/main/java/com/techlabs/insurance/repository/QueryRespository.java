package com.techlabs.insurance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.insurance.entities.Customer;
import com.techlabs.insurance.entities.Query;


public interface QueryRespository extends JpaRepository<Query, Integer>{
	
	Page<Query> findAll(Pageable pageable);
	Page<Query> findByCustomer(Customer customer, Pageable pageable);
}
