package com.techlabs.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.QueryDto;
import com.techlabs.insurance.service.QueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class QueryController {
	
	@Autowired
	private QueryService queryService;
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/addquery/{customerid}")
	ResponseEntity<?> addQuery(@PathVariable(name = "customerid") int customerid, @RequestBody QueryDto queryDto){
		String response  = queryService.addQuery(customerid, queryDto);
		return new ResponseEntity<> (response, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('EMPLOYEE') || hasRole('ADMIN')")
	@GetMapping("/getqueries")
	ResponseEntity<?> getQuery(@RequestParam int pageno, @RequestParam int pagesize){
		Page<QueryDto> response = queryService.getQueries(pageno, pagesize);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('EMPLOYEE') || hasRole('ADMIN')")
	@PutMapping("/updateresponse/{queryid}")
	ResponseEntity<?> getQuery(@PathVariable int queryid, @RequestBody QueryDto queryDto){
		String response = queryService.updateQueryResponse(queryid, queryDto);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getqueries/{customerid}")
	ResponseEntity<?> getQuery(@PathVariable int customerid, @RequestParam int pageno, @RequestParam int pagesize){
		Page<QueryDto> response = queryService.getQueriesByCustomer(customerid, pageno, pagesize);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
