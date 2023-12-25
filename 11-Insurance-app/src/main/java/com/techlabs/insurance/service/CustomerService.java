package com.techlabs.insurance.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.techlabs.insurance.dto.CustomerDto;
import com.techlabs.insurance.dto.QueryDto;
import com.techlabs.insurance.entities.Customer;

public interface CustomerService {
	
    void addCustomer(CustomerDto customerDto);
	Customer getCustomerById(int customerId);

	
//	Page<CustomerDto> getAllCustomer(Pageable pageable, String keyword);
	Page<CustomerDto> getAllCustomer(int pageno, int pagesize);
	
	Page<CustomerDto> getCustomerSearch(int pageNumber, int pageSize, String keyword);
}
