package com.techlabs.insurance.dto;

import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.entities.SchemeDetails;
import com.techlabs.insurance.entities.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class InsurenceSchemeDto {
	
	private int planId;
	private int schemeId;
	private String schemeName;
	private MultipartFile image;
	private SchemeDetails schemeDetails;
	private Status status;
}
