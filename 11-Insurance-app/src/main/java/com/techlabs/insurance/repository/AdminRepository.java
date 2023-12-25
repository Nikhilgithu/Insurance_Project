package com.techlabs.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techlabs.insurance.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer>{

}
