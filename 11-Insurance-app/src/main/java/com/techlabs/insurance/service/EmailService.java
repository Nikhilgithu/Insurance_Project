package com.techlabs.insurance.service;

import com.techlabs.insurance.dto.EmailDto;

public interface EmailService {
	public String sendMail(EmailDto details);
}
