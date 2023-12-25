package com.techlabs.insurance.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.dto.InsurencePlanDto;
import com.techlabs.insurance.entities.InsurancePlan;
import com.techlabs.insurance.entities.Status;
import com.techlabs.insurance.exceptions.InsurancePlanNotFoundException;
import com.techlabs.insurance.repository.InsurencePlanRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class InsurancePlanServiceImpl implements InsurancePlanService {

	private final Logger logger = LoggerFactory.getLogger(InsurancePlanServiceImpl.class);

	@Autowired
	private InsurencePlanRepository insurencePlanRepo;

	@Override
	public void addInsurancePlan(InsurencePlanDto insurancePlanDto) {
		InsurancePlan insurancePlan = new InsurancePlan();
		insurancePlan.setPlanName(insurancePlanDto.getPlanname());
		insurancePlan.setStatus(Status.ACTIVE);

		insurencePlanRepo.save(insurancePlan);
		logger.info("addInsurancePlan:Added insurance plan successfully.");
	}

	@Override
	public List<InsurencePlanDto> getAllInsurencePlans() {

		List<InsurancePlan> insurancePlans = insurencePlanRepo.findAll();
		List<InsurencePlanDto> insurancePlanDtos = new ArrayList<>();

		for (InsurancePlan insurancePlan : insurancePlans) {
			InsurencePlanDto insurancePlanDto = new InsurencePlanDto();
			insurancePlanDto.setPlanid(insurancePlan.getPlanId());
			insurancePlanDto.setPlanname(insurancePlan.getPlanName());
			insurancePlanDto.setStatus(insurancePlan.getStatus());
			insurancePlanDtos.add(insurancePlanDto);
		}

		logger.info("getAllInsurencePlans:Retrieved all insurance plans successfully.");
		return insurancePlanDtos;
	}

	@Override
	public void updateInsurancePlan(int id, InsurencePlanDto updatedInsurancePlanDto) {
		InsurancePlan existingPlan = insurencePlanRepo.findById(id)
				.orElseThrow(() -> new InsurancePlanNotFoundException("Insurance plan not found with ID: " + id));

		existingPlan.setPlanName(updatedInsurancePlanDto.getPlanname());
		existingPlan.setStatus(updatedInsurancePlanDto.getStatus());

		insurencePlanRepo.save(existingPlan);
		logger.info("updateInsurancePlan:Updated insurance plan with ID: {} successfully.", id);
	}

	@Override
	public void deleteInsurancePlanById(int id) {
		insurencePlanRepo.deleteById(id);
		logger.info("deleteInsurancePlanById:Deleted insurance plan with ID: {} successfully.", id);
	}

	@Override
	public List<InsurencePlanDto> getActiveInsurancePlans() {
		List<InsurancePlan> activePlans = insurencePlanRepo.findByStatus(Status.ACTIVE);
		List<InsurencePlanDto> activePlanDtos = new ArrayList<>();

		for (InsurancePlan plan : activePlans) {
			InsurencePlanDto planDto = new InsurencePlanDto();
			planDto.setPlanid(plan.getPlanId());
			planDto.setPlanname(plan.getPlanName());
			planDto.setStatus(plan.getStatus());
			activePlanDtos.add(planDto);
		}

		logger.info("getActiveInsurancePlans:Retrieved all active insurance plans successfully.");
		return activePlanDtos;
	}

	@Override
	public Page<InsurencePlanDto> getAllInsurencePlansPageWise(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<InsurancePlan> insurancePlansPage = insurencePlanRepo.findAll(pageable);

		logger.info("getAllInsurencePlansPageWise:Retrieved insurance plans page {} of size {} successfully.",
				pageNumber, pageSize);
		return insurancePlansPage.map(insurancePlan -> {
			InsurencePlanDto insurancePlanDto = new InsurencePlanDto();
			insurancePlanDto.setPlanid(insurancePlan.getPlanId());
			insurancePlanDto.setPlanname(insurancePlan.getPlanName());
			insurancePlanDto.setStatus(insurancePlan.getStatus());
			return insurancePlanDto;
		});
	}

}
