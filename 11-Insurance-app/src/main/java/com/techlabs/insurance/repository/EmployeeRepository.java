package com.techlabs.insurance.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techlabs.insurance.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

	Optional<Employee> findById(int employeeId);

	void deleteById(int employeeId);

}
