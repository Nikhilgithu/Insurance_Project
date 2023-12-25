package com.techlabs.insurance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.entities.SchemeDetails;
import com.techlabs.insurance.repository.SchemeDetailsRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class SchemeDetailsServiceImpl implements SchemeDetailsService{

	 @Autowired
	    private SchemeDetailsRepository schemeDetailsRepo;
	
	 
}
