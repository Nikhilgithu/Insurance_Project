package com.techlabs.insurance.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.dto.LoginDto;
import com.techlabs.insurance.dto.RegisterDto;
import com.techlabs.insurance.entities.Admin;
import com.techlabs.insurance.entities.Agent;
import com.techlabs.insurance.entities.Customer;
import com.techlabs.insurance.entities.Employee;
import com.techlabs.insurance.entities.Role;
import com.techlabs.insurance.entities.Status;
import com.techlabs.insurance.entities.User;
import com.techlabs.insurance.exceptions.AccountNotFoundException;
import com.techlabs.insurance.exceptions.AccountStatusException;
import com.techlabs.insurance.exceptions.UserAPIException;
import com.techlabs.insurance.repository.AdminRepository;
import com.techlabs.insurance.repository.AgentRepository;
import com.techlabs.insurance.repository.CustomerRepository;
import com.techlabs.insurance.repository.EmployeeRepository;
import com.techlabs.insurance.repository.RoleRepository;
import com.techlabs.insurance.repository.UserRepository;
import com.techlabs.insurance.security.JwtTokenProvider;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private AgentRepository agentRepository;
	@Autowired
	private CustomerRepository customerRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	@Override
	public String login(LoginDto loginDto) {
		Authentication authentication = null;
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		}catch(Exception e) {
			if(authentication == null)
				throw new AccountNotFoundException("Invalid username or password");
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		User user = userService.getUser(loginDto.getUsername());
		String rolename = user.getRole().getRoleName();
		if(rolename.equals("ROLE_AGENT")) {
			List<Agent> agents = agentRepository.findByUser(user);
			if(agents.get(0).getStatus().equals(Status.PENDING)) {
				throw new AccountStatusException("Your account is still not verified.\n "
						+ "You can login after it get verified.\n");
			}
			if(agents.get(0).getStatus().equals(Status.INACTIVE)) {
				throw new AccountStatusException("Your account is inactive.\n "
						+ "Kindly contact to higher authority.\n");
			}
		}
		
		String token = jwtTokenProvider.generateToken(authentication, rolename);
		logger.info("Token generated Successfully");
		logger.info("Login successfull");
		return token;
	}
	
	private User createUser(RegisterDto registerDto) {
		User user = new User();
		user.setUsername(registerDto.getUsername());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		Role userRole = roleRepository.findById(registerDto.getRoleid()).orElse(null);
		user.setRole(userRole);
		return user;
	}

	
	@Override
	public String adminRegister(RegisterDto registerDto){
		if(userRepository.existsByUsername(registerDto.getUsername()))
			throw new UserAPIException("User Already exists");
		
		User user = createUser(registerDto);
		Admin admin = new Admin();
		
		admin.setFirstName(registerDto.getFirstname());
		admin.setLastName(registerDto.getLastname());
		admin.setUser(user);
		adminRepository.save(admin);

		logger.info("Admin Register Successfully");
		return "Admin Register Successfully";
	}
	
	@Override
	public String employeeRegister(RegisterDto registerDto) {
		if(userRepository.existsByUsername(registerDto.getUsername()))
			throw new UserAPIException("User Already exists");
		User user = createUser(registerDto);
		Employee employee = new Employee();
		employee.setFirstname(registerDto.getFirstname());
		employee.setLastname(registerDto.getLastname());
		employee.setSalary(registerDto.getSalary());
		employee.setUser(user);
		employeeRepository.save(employee);
		logger.info("Employee Register Successfully");
		return "Employee Register Successfully";
	}
	
	@Override
	public String agentRegister(RegisterDto registerDto) {
		if(userRepository.existsByUsername(registerDto.getUsername()))
			throw new UserAPIException("User Already exists");
		User user = createUser(registerDto);
		Agent agent = new Agent();
		agent.setFirstname(registerDto.getFirstname());
		agent.setLastname(registerDto.getLastname());
		agent.setQualification(registerDto.getQualification());
		agent.setStatus(Status.PENDING);
		agent.setCommissionEarn(0.0);
		agent.setUser(user);
		agentRepository.save(agent);
		logger.info("Agent Register Successfully");
		return "Agent Register Successfully";
	}
	
	@Override
	public String customerRegister(RegisterDto registerDto) {
		if(userRepository.existsByUsername(registerDto.getUsername()))
			throw new UserAPIException("User Already exists");
		User user = createUser(registerDto);
		Customer customer = new Customer();
		customer.setFirstname(registerDto.getFirstname());
		customer.setLastname(registerDto.getLastname());
		customer.setAge(registerDto.getAge());
		customer.setEmail(registerDto.getEmail());
		customer.setMobileNo(registerDto.getMobileNo());
		customer.setState(registerDto.getState());
		customer.setCity(registerDto.getCity());
		customer.setUser(user);
		customerRepository.save(customer);
		logger.info("Customer Register Successfully");
		return "Customer Register Successfully";
	}
	
	@Override
	public String getRoleFromToken(String token) {
		Claims claims = jwtTokenProvider.decodeJwtToken(token);
		return claims.get("role", String.class);
	}
	
	public String getUsernameFromToken(String token) {
		Claims claims = jwtTokenProvider.decodeJwtToken(token);
		return claims.getSubject();
	}

}
