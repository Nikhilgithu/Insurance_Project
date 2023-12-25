package com.techlabs.insurance.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.insurance.entities.InsuranceScheme;
import com.techlabs.insurance.entities.Status;

public interface InsurenceSchemeRepository extends JpaRepository<InsuranceScheme,Integer>{
    List<InsuranceScheme> findByStatus(Status status);
    Page<InsuranceScheme> findByStatus(Status status, Pageable pageable);
    List<InsuranceScheme> findByschemeNameContaining(String Keywords);

}
