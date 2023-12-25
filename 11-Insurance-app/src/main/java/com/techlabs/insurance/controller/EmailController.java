package com.techlabs.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.EmailDto;
import com.techlabs.insurance.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class EmailController {
	@Autowired 
	private EmailService emailService;
	 
    // Sending a simple Email
    @PostMapping("/sendmail")
    public ResponseEntity<?> sendMail(@RequestBody EmailDto details)
    {
        String status = emailService.sendMail(details);
        return new ResponseEntity<> (status, HttpStatus.ACCEPTED);
    }
}
