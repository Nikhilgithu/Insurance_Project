package com.techlabs.insurance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.dto.EmailDto;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{
	
	@Autowired
	private JavaMailSender mailSender;
	
	 @Value("${spring.mail.username}")
	  private String defaultSender;
	
	private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	 
	public String sendMail(EmailDto details) {
       try {
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            if (details.getSender() != null && !details.getSender().isEmpty()) {
            	logger.info("Using sender from EmailDto: " + details.getSender());
                message.setFrom(details.getSender());
            } else {
            	logger.info("Using default sender");
                message.setFrom(defaultSender);
            }
            helper.setTo(details.getRecipients().toArray(new String[0]));
            helper.setSubject(details.getSubject());
            helper.setText(details.getMsgBody(), true);
            
            mailSender.send(message);
            
            return "Mail Sent Successfully";
        } catch (Exception e) {
            return "Error while Sending Mail: " + e.getMessage();
        }
	}
}
