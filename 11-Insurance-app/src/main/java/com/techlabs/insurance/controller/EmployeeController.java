package com.techlabs.insurance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.EmployeeDto;
import com.techlabs.insurance.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getallemployees")
	List<EmployeeDto> getAllEmployee() {
		return employeeService.getAllEmployee();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getemployee/{id}")
	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable int employeeId) {
		EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);

		if (employeeDto != null) {
			return ResponseEntity.ok(employeeDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteemployee/{employeeId}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable int employeeId) {
        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.ok("Employee with ID " + employeeId + " has been deleted.");
    }
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updateemployee/{employeeId}")
    public ResponseEntity<?> updateEmployeeById(
            @PathVariable int employeeId, @RequestBody EmployeeDto updatedEmployee) {
        String response = employeeService.updateEmployeeById(employeeId, updatedEmployee);
        return new ResponseEntity<> (response, HttpStatus.ACCEPTED);
    }
	
	@PreAuthorize("hasRole('ADMIN')")
 	@GetMapping("/getemployees")
    public ResponseEntity<Page<EmployeeDto>> getAllEmployeePageWise(
            @RequestParam(defaultValue = "0") int pageno,
            @RequestParam(defaultValue = "10") int pagesize) {
        Page<EmployeeDto> employees = employeeService.getAllEmployeePageWise(pageno, pagesize);
        return ResponseEntity.ok(employees);
    }
}
