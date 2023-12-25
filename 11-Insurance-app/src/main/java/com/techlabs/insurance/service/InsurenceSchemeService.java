package com.techlabs.insurance.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.techlabs.insurance.dto.InsurenceSchemeDto;
import com.techlabs.insurance.entities.InsuranceScheme;
import com.techlabs.insurance.entities.Status;

public interface InsurenceSchemeService {
	void addInsurenceScheme(InsurenceSchemeDto insuranceSchemeDto);
	List<InsurenceSchemeDto> getAllInsurenceSchemes(String keyword);
	    void updateInsurenceScheme(int schemeId, InsurenceSchemeDto insuranceSchemeDto);
	    void deleteInsurenceSchemeById(int id);
//	    List<InsurenceSchemeDto> getActiveInsurenceSchemes();
	    List<InsurenceSchemeDto> getActiveSchemesandPlans();
	    public List<InsurenceSchemeDto> getActiveSchemesByPlanid(int planid);
	    InsuranceScheme getInsuranceSchemeById(int schemeId);
	    Page<InsurenceSchemeDto> getAllInsurenceSchemesPageWise(int pageNumber, int pageSize);
	    Page<InsurenceSchemeDto> getInsurenceSchemesByStatus(Status status, int pageno, int pagesize);
	    public void updateInsurenceSchemeStatus(int schemeId, Status newStatus);
		
}
