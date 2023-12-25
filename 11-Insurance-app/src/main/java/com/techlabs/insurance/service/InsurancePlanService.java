package com.techlabs.insurance.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.techlabs.insurance.dto.InsurencePlanDto;

public interface InsurancePlanService {
	 void addInsurancePlan(InsurencePlanDto insurancePlanDto);
	 List<InsurencePlanDto> getAllInsurencePlans();
	 void updateInsurancePlan(int id, InsurencePlanDto updatedInsurancePlan);
	 void deleteInsurancePlanById(int id);
	 List<InsurencePlanDto> getActiveInsurancePlans();
	 Page<InsurencePlanDto> getAllInsurencePlansPageWise(int pageNumber, int pageSize);
}
