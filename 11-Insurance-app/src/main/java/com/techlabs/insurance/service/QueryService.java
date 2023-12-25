package com.techlabs.insurance.service;

import org.springframework.data.domain.Page;

import com.techlabs.insurance.dto.QueryDto;
import com.techlabs.insurance.entities.Customer;

public interface QueryService {
	String addQuery(int customerid, QueryDto queryDto);
	Page<QueryDto> getQueries(int pageno, int pagesize);
	Page<QueryDto> getQueriesByCustomer(int customerid, int pageno, int pagesize);
	String updateQueryResponse(int queryid, QueryDto queryDto);
}
