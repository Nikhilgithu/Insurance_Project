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

import com.techlabs.insurance.dto.QueryDto;
import com.techlabs.insurance.entities.Customer;
import com.techlabs.insurance.entities.Query;
import com.techlabs.insurance.exceptions.AccountNotFoundException;
import com.techlabs.insurance.repository.CustomerRepository;
import com.techlabs.insurance.repository.QueryRespository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class QueryServiceImpl implements QueryService{
	
	private static final Logger logger = LoggerFactory.getLogger(QueryServiceImpl.class);
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private QueryRespository queryRepository; 

	 @Override
	    public String addQuery(int customerid, QueryDto queryDto) {
	        Customer customer = customerRepo.findById(customerid).orElse(null);
	        if (customer == null)
	            throw new AccountNotFoundException("Account not found with id: " + customerid);

	        Query query = new Query();
	        query.setSubject(queryDto.getSubject());
	        query.setQuestion(queryDto.getQuestion());
	        List<Query> queries = customer.getQueries();
	        queries.add(query);
	        customer.setQueries(queries);
	        query.setCustomer(customer);
	        queryRepository.save(query);

	        logger.info("addQuery:Query submitted successfully for customer ID: {}", customerid);
	        return "Your query is submitted.\nIf you have more queries, you can ask.";
	    }

	    @Override
	    public Page<QueryDto> getQueries(int pageno, int pagesize) {
	        Pageable pageable = PageRequest.of(pageno, pagesize, Sort.by("queryId").descending());
	        Page<Query> queries = queryRepository.findAll(pageable);
	        Page<QueryDto> queriesDto = queries.map((query) -> {
	            QueryDto queryDto = new QueryDto();
	            queryDto.setQueryId(query.getQueryId());
	            queryDto.setSubject(query.getSubject());
	            queryDto.setQuestion(query.getQuestion());
	            queryDto.setAnswer(query.getAnswer());
	            return queryDto;
	        });

	        logger.info("getQueries:Retrieved all queries successfully.");
	        return queriesDto;
	    }

	    @Override
	    public Page<QueryDto> getQueriesByCustomer(int customerid, int pageno, int pagesize) {
	        Customer customer = customerRepo.findById(customerid)
	                .orElseThrow(() -> new AccountNotFoundException("No customer found with id: " + customerid));

	        Pageable pageable = PageRequest.of(pageno, pagesize, Sort.by("queryId").descending());
	        Page<Query> queries = queryRepository.findByCustomer(customer, pageable);
	        Page<QueryDto> queriesDto = queries.map((query) -> {
	            QueryDto queryDto = new QueryDto();
	            queryDto.setQueryId(query.getQueryId());
	            queryDto.setSubject(query.getSubject());
	            queryDto.setQuestion(query.getQuestion());
	            queryDto.setAnswer(query.getAnswer());
	            return queryDto;
	        });

	        logger.info("getQueriesByCustomer:Retrieved queries for customer ID: {} successfully.", customerid);
	        return queriesDto;
	    }

	    @Override
	    public String updateQueryResponse(int queryid, QueryDto queryDto) {
	        Query query = queryRepository.findById(queryid).orElse(null);
	        if (query == null)
	            throw new AccountNotFoundException("Query not found with id: " + queryid);

	        query.setAnswer(queryDto.getAnswer());
	        queryRepository.save(query);

	        logger.info("updateQueryResponse:Updated query response for query ID: {}", queryid);
	        return "Your response has been submitted successfully.";
	    }

}
