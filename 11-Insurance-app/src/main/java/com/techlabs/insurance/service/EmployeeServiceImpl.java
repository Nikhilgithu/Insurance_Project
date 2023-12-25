package com.techlabs.insurance.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.dto.EmployeeDto;
import com.techlabs.insurance.entities.Employee;
import com.techlabs.insurance.exceptions.UserAPIException;
import com.techlabs.insurance.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
	
	private final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeRepository employeeRepo;
//	@Autowired
//	private UserRepository userRepository;

	  @Override
	    public List<EmployeeDto> getAllEmployee() {
	        List<Employee> employees = employeeRepo.findAll();
	        List<EmployeeDto> employeeDtos = new ArrayList<>();
	        for (Employee employee : employees) {
	            EmployeeDto Employeedto = new EmployeeDto();
	            Employeedto.setEmployeeId(employee.getEmployeeId());
	            Employeedto.setFirstname(employee.getFirstname());
	            Employeedto.setLastname(employee.getLastname());
	            Employeedto.setSalary(employee.getSalary());

	            employeeDtos.add(Employeedto);
	        }
	        logger.info("getAllEmployee:Retrieved all employees successfully.");
	        return employeeDtos;
	    }

	  @Override
	    public EmployeeDto getEmployeeById(int employeeId) {
	        Employee employee = employeeRepo.findById(employeeId).orElse(null);

	        if (employee != null) {
	            return new EmployeeDto(employee.getEmployeeId(), employee.getFirstname(), employee.getLastname(),
	                    employee.getSalary());
	        }

	        return null;
	    }

	    @Override
	    public void deleteEmployeeById(int employeeId) {
	        employeeRepo.deleteById(employeeId);
	        logger.info("deleteEmployeeById:Deleted employee with ID: {} successfully.", employeeId);
	    }

	
	    @Override
	    public String updateEmployeeById(int employeeId, EmployeeDto updatedEmployee) {
	        Employee existingEmployee = employeeRepo.findById(employeeId)
	                .orElseThrow(() -> new UserAPIException("Employee not found"));

	        existingEmployee.setFirstname(updatedEmployee.getFirstname());
	        existingEmployee.setLastname(updatedEmployee.getLastname());
	        existingEmployee.setSalary(updatedEmployee.getSalary());

	        Employee updatedEntity = employeeRepo.save(existingEmployee);

	        logger.info("updateEmployeeById:Updated employee with ID: {} successfully.", employeeId);
	        return "Employee updated successfully";
	    }

	    @Override
	    public Page<EmployeeDto> getAllEmployeePageWise(int pageNumber, int pageSize) {
	        Pageable pageable = PageRequest.of(pageNumber, pageSize);
	        Page<Employee> employeesPage = employeeRepo.findAll(pageable);

	        logger.info("getAllEmployeePageWise:Retrieved employees page {} of size {} successfully.", pageNumber, pageSize);
	        return employeesPage.map(employee -> {
	            EmployeeDto employeeDto = new EmployeeDto();
	            employeeDto.setEmployeeId(employee.getEmployeeId());
	            employeeDto.setFirstname(employee.getFirstname());
	            employeeDto.setLastname(employee.getLastname());
	            employeeDto.setSalary(employee.getSalary());
	            return employeeDto;
	        });
	    }


}
