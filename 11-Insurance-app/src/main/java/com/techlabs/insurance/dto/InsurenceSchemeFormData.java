package com.techlabs.insurance.dto;

import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.entities.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class InsurenceSchemeFormData {
	private String schemeName;
    private MultipartFile image;
    private String description;
    private double minAmount;
    private double maxAmount;
    private int minInvestment;
    private int maxInvestment;
    private int minAge;
    private int maxAge;
    private double profitRatio;
    private double registrationCommission;
    private double installmentCommission;
    private Status status;
}
