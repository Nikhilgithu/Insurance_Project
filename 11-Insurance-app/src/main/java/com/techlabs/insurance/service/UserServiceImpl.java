package com.techlabs.insurance.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.dto.UserDto;
import com.techlabs.insurance.entities.Admin;
import com.techlabs.insurance.entities.Agent;
import com.techlabs.insurance.entities.Customer;
import com.techlabs.insurance.entities.Employee;
import com.techlabs.insurance.entities.User;
import com.techlabs.insurance.exceptions.UserAPIException;
import com.techlabs.insurance.repository.AdminRepository;
import com.techlabs.insurance.repository.AgentRepository;
import com.techlabs.insurance.repository.CustomerRepository;
import com.techlabs.insurance.repository.EmployeeRepository;
import com.techlabs.insurance.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private AgentRepository agentRepository;
	@Autowired
	private CustomerRepository customerRepository;
	
	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	
    @Override
    public User getUser(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null)
            throw new UserAPIException("User not found with username: " + username);
        
        logger.info("User with username: {} found", username);
        return user;
    }

    @Override
    public UserDto getUserDto(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null)
            throw new UserAPIException("User not found with username: " + username);
        
        UserDto userDto = new UserDto();
        userDto.setUserid(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setRoleid(user.getRole().getRoleId());
        userDto.setRole(user.getRole().getRoleName());
        
        if (user.getRole().getRoleName().equals("ROLE_ADMIN")) {
            List<Admin> adminList = adminRepository.findAll();
            for (Admin e : adminList) {
                if (e.getUser().getUsername().equals(username)) {
                    userDto.setId(e.getAdminId());
                    userDto.setFirstname(e.getFirstName());
                    userDto.setLastname(e.getLastName());
                }
            }
        }
        if (user.getRole().getRoleName().equals("ROLE_EMPLOYEE")) {
            List<Employee> employeeList = employeeRepository.findAll();
            for (Employee e : employeeList) {
                if (e.getUser().getUsername().equals(username)) {
                    userDto.setId(e.getEmployeeId());
                    userDto.setFirstname(e.getFirstname());
                    userDto.setLastname(e.getLastname());
                }
            }
        }
        if (user.getRole().getRoleName().equals("ROLE_AGENT")) {
            List<Agent> agentList = agentRepository.findAll();
            for (Agent e : agentList) {
                if (e.getUser().getUsername().equals(username)) {
                    userDto.setId(e.getAgentId());
                    userDto.setFirstname(e.getFirstname());
                    userDto.setLastname(e.getLastname());
                }
            }
        }
        if (user.getRole().getRoleName().equals("ROLE_CUSTOMER")) {
            List<Customer> customerList = customerRepository.findAll();
            for (Customer e : customerList) {
                if (e.getUser().getUsername().equals(username)) {
                    userDto.setId(e.getCustomerId());
                    userDto.setFirstname(e.getFirstname());
                    userDto.setLastname(e.getLastname());
                    userDto.setAge(e.getAge());
                }
            }
        }
        logger.info("getUserDto:Retrieved user details for username: {}", username);
        return userDto;
    }


}
