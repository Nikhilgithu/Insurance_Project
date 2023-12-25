package com.techlabs.insurance.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.insurance.entities.Agent;
import com.techlabs.insurance.entities.Status;
import com.techlabs.insurance.entities.User;


public interface AgentRepository extends JpaRepository<Agent, Integer>{
	List<Agent> findByUser(User user);
	List<Agent> findByStatus(Status status);
	Page<Agent> findByStatus(Status status, Pageable pageable);
}
