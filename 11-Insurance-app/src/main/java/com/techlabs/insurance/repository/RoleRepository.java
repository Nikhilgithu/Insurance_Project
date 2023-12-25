package com.techlabs.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techlabs.insurance.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

}
