package com.techlabs.insurance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.insurance.entities.InsurancePlan;
import com.techlabs.insurance.entities.Status;

public interface InsurencePlanRepository extends JpaRepository<InsurancePlan,Integer>{
	 List<InsurancePlan> findByStatus(Status active);
}
