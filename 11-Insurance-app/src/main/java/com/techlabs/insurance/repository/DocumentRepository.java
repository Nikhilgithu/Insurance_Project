package com.techlabs.insurance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.insurance.entities.Claim;
import com.techlabs.insurance.entities.Document;
import com.techlabs.insurance.entities.InsurancePolicy;


public interface DocumentRepository extends JpaRepository<Document, Integer>{
	List<Document> findByInsurancePolicy(InsurancePolicy insurancePolicy);
	List<Document> findByClaim(Claim claim);
}
