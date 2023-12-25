package com.techlabs.insurance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.dto.InsurancePolicyDto;
import com.techlabs.insurance.entities.Status;

public interface InsurencePolicyService {
	void addInsurancePolicy(InsurancePolicyDto insurancePolicyDto, List<MultipartFile> documentFiles);
	Page<InsurancePolicyDto> getPoliciesByStatus(Status status, int pageno, int pagesize);
	String updatePolicyStatusByPolicyNo(int policyNo, Status newStatus);
	Page<InsurancePolicyDto> getInsurancePoliciesByCustomerIdAndDateRange(
            int customerId, LocalDate startDate, LocalDate endDate,
            int pageNumber, int pageSize);
	Page<InsurancePolicyDto> getInsurancePoliciesByCustomerId(int customerId, int pageNumber, int pageSize);
}
