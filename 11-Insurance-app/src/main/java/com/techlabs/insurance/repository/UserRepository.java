package com.techlabs.insurance.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techlabs.insurance.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
}
