package com.techlabs.insurance.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.dto.CustomerDto;
import com.techlabs.insurance.entities.Customer;
import com.techlabs.insurance.entities.InsurancePolicy;
import com.techlabs.insurance.repository.CustomerRepository;
import com.techlabs.insurance.repository.InsurencePolicyRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService{
	
	private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private InsurencePolicyRepository insurencePolicyRepository;

	@Override
	public Page<CustomerDto> getAllCustomer(int pageno, int pagesize) {
		Pageable pageable = PageRequest.of(pageno, pagesize, Sort.by("customerId").descending());
		Page<Customer> customers = customerRepo.findAll(pageable);
        Page<CustomerDto> customerDTOs = customers.map((customer)->{
        	CustomerDto customerDTO = new CustomerDto();
            customerDTO.setCustomerId(customer.getCustomerId());
            customerDTO.setFirstname(customer.getFirstname());
            customerDTO.setLastname(customer.getLastname());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setMobileNo(customer.getMobileNo());
            customerDTO.setState(customer.getState());
            customerDTO.setCity(customer.getCity());
            List<InsurancePolicy> policies = insurencePolicyRepository.findByCustomer(customer);
            customerDTO.setNumberOfPolicies(policies.size());
            return customerDTO;
        });
        logger.info("getAllCustomer: Retrieved all customers successfully with pageno: {} and pagesize: {}.", pageno, pagesize);
        return customerDTOs;

	}

	@Override
	public Page<CustomerDto> getCustomerSearch(int pageNumber, int pageSize, String keyword) {
	    Pageable pageable = PageRequest.of(pageNumber, pageSize);
	    Page<Customer> customerPage;

	    if (keyword != null && !keyword.isEmpty()) {
	     
	            customerPage = customerRepo.findByFirstnameContainingOrMobileNoContainingOrEmailContainingOrLastnameContainingOrCityContainingOrStateContaining(
	                keyword, keyword, keyword, keyword, keyword, keyword, pageable);
	        
	    } else {
	        customerPage = customerRepo.findAll(pageable);
	    }

	    Page<CustomerDto> customerDtoPage = customerPage.map(customer -> {
	        CustomerDto customerDto = new CustomerDto();
	        customerDto.setCustomerId(customer.getCustomerId());
	        customerDto.setFirstname(customer.getFirstname());
	        customerDto.setLastname(customer.getLastname());
	        customerDto.setEmail(customer.getEmail());
	        customerDto.setMobileNo(customer.getMobileNo());
	        customerDto.setState(customer.getState());
	        customerDto.setCity(customer.getCity());
	        customerDto.setNumberOfPolicies(customer.getPolicies().size());
	        return customerDto;
	    });

	    logger.info("getAllCustomers: Retrieved customers successfully.");

	    return customerDtoPage;
	}





    @Override
    public void addCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setCustomerId(customerDto.getCustomerId());
        customer.setFirstname(customerDto.getFirstname());
        customer.setLastname(customerDto.getLastname());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNo(customerDto.getMobileNo());
        customer.setState(customerDto.getState());
        customer.setCity(customerDto.getCity());
        customerRepo.save(customer);
        logger.info("addCustomer: Added customer with ID: {} successfully.", customerDto.getCustomerId());
    }

    @Override
    public Customer getCustomerById(int customerId) {
        return customerRepo.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer with ID " + customerId + " not found"));
    }
}
