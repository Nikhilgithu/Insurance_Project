package com.techlabs.insurance.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.techlabs.insurance.dto.EmployeeDto;

public interface EmployeeService {
 List<EmployeeDto> getAllEmployee();
 EmployeeDto getEmployeeById(int employeeId);
 void deleteEmployeeById(int employeeId);
 String updateEmployeeById(int employeeId, EmployeeDto updatedEmployee);
 Page<EmployeeDto> getAllEmployeePageWise(int pageNumber, int pageSize);

}
