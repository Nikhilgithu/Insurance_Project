package com.techlabs.insurance.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.dto.CustomerDto;
import com.techlabs.insurance.dto.InsurancePolicyDto;
import com.techlabs.insurance.dto.InsurenceSchemeDto;
import com.techlabs.insurance.dto.NomineeDto;
import com.techlabs.insurance.entities.Agent;
import com.techlabs.insurance.entities.Customer;
import com.techlabs.insurance.entities.Document;
import com.techlabs.insurance.entities.InsurancePolicy;
import com.techlabs.insurance.entities.InsuranceScheme;
import com.techlabs.insurance.entities.Nominee;
import com.techlabs.insurance.entities.SchemeDetails;
import com.techlabs.insurance.entities.Status;
import com.techlabs.insurance.exceptions.PolicyNotFoundException;
import com.techlabs.insurance.repository.InsurencePolicyRepository;

import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@AllArgsConstructor
@Service
@Slf4j
public class InsurencePolicyServiceImpl implements InsurencePolicyService {
	@Autowired
	private InsurencePolicyRepository policyRepository;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private InsurenceSchemeService insuranceSchemeService;
	@Autowired
	private AgentService agentService;
	
	private static final Logger logger = LoggerFactory.getLogger(InsurencePolicyServiceImpl.class);

	@Override
	@Transactional
	public void addInsurancePolicy(InsurancePolicyDto insurancePolicyDto, List<MultipartFile> documentFiles) {
		InsurancePolicy insurancePolicy = new InsurancePolicy();
		insurancePolicy.setIssueDate(insurancePolicyDto.getIssueDate());
		insurancePolicy.setMaturityDate(insurancePolicyDto.getMaturityDate());
		insurancePolicy.setPremiumType(insurancePolicyDto.getPremiumType());
		insurancePolicy.setPremiumamount(insurancePolicyDto.getPremiumAmount());
		insurancePolicy.setSumAssured(insurancePolicyDto.getSumAssured());
		insurancePolicy.setStatus(insurancePolicyDto.getStatus());
		insurancePolicy.setInvestamount(insurancePolicyDto.getInvestAmount());
		insurancePolicy.setTotalinstallment(insurancePolicyDto.getTotalInstallment());

		Customer customer = customerService.getCustomerById(insurancePolicyDto.getCustomer().getCustomerId());
		insurancePolicy.setCustomer(customer);

		InsuranceScheme insuranceScheme = insuranceSchemeService
				.getInsuranceSchemeById(insurancePolicyDto.getInsuranceScheme().getSchemeId());
		insurancePolicy.setInsuranceScheme(insuranceScheme);

		int agentId = insurancePolicyDto.getAgent();
		if (agentId != 0) {
			Agent agent = agentService.getAgentById(agentId);
			List<InsurancePolicy> agentInsurancePolicies = agent.getInsurancePolicies();

			agentInsurancePolicies.add(insurancePolicy);
			agent.setInsurancePolicies(agentInsurancePolicies);
		}

		List<NomineeDto> nomineeDtos = insurancePolicyDto.getNominees();
		List<Nominee> nominees = new ArrayList<>();

		for (NomineeDto nomineeDto : nomineeDtos) {
			Nominee nominee = new Nominee();
	        nominee.setNomineeName(nomineeDto.getNomineeName());
	        nominee.setNomineeRelation(nomineeDto.getNomineeRelation());
	        logger.warn(nomineeDto.getNomineeName());
	        nominee.setInsurancePolicy(insurancePolicy);
	        nominees.add(nominee);
		}
		insurancePolicy.setNominees(nominees);
		
		List<Document> documents = new ArrayList<>();
		for (MultipartFile documentFile : insurancePolicyDto.getDocumentFiles()) {
			if ("application/pdf".equals(documentFile.getContentType())) {
				Document document = new Document();
				document.setDocumentType("Policy Document");
				document.setDocumentName(documentFile.getOriginalFilename());
				try {
					try {
						document.setDocumentFile(documentFile.getBytes());
					} catch (java.io.IOException e) {
						e.printStackTrace();
					}
					document.setInsurancePolicy(insurancePolicy);
					documents.add(document);
				} catch (IOException e) {
					logger.error("addInsurancePolicy:Error while processing document: {}", e.getMessage());
				}
			} else {

				logger.error("addInsurancePolicy:Uploaded document is not a PDF");
			}
		}

		insurancePolicy.setDocuments(documents);
		
		policyRepository.save(insurancePolicy);
		
		logger.info("Insurance policy added successfully");
	}

	@Override
	public Page<InsurancePolicyDto> getPoliciesByStatus(Status status, int pageno, int pagesize) {
		Pageable pageable = PageRequest.of(pageno, pagesize);
        Page<InsurancePolicy> policies = policyRepository.findByStatus(status, pageable);
        Page<InsurancePolicyDto> policiesDto = policies.map((policy) ->{
        	InsurancePolicyDto dto = new InsurancePolicyDto();
        	dto.setPolicyid(policy.getPolicyNo());
        	dto.setInvestAmount(policy.getInvestamount());
            dto.setIssueDate(policy.getIssueDate());
            dto.setMaturityDate(policy.getMaturityDate());
            dto.setPremiumType(policy.getPremiumType());
            dto.setPremiumAmount(policy.getPremiumamount());
            dto.setSumAssured(policy.getSumAssured());
            dto.setStatus(policy.getStatus());
            dto.setDocumentFiles(dto.getDocumentFiles());
            InsurenceSchemeDto isDto = new InsurenceSchemeDto();
            isDto.setSchemeId(policy.getInsuranceScheme().getSchemeId());
            isDto.setSchemeName(policy.getInsuranceScheme().getSchemeName());
            dto.setInsuranceScheme(isDto);
            CustomerDto cusDto = new CustomerDto();
            cusDto.setCustomerId(policy.getCustomer().getCustomerId());
            cusDto.setEmail(policy.getCustomer().getEmail());
            dto.setCustomer(cusDto);
            return dto;
        });
        logger.info("getPoliciesByStatus:All policies with status: " + status + " fetched.");
        return policiesDto;
	}

	@Override
	@Transactional
	  public String updatePolicyStatusByPolicyNo(int policyNo, Status newStatus) {
        InsurancePolicy policy = policyRepository.findBypolicyNo(policyNo);

        if(policy == null){
        	throw new PolicyNotFoundException("Policy with policy number " + policyNo + " not found");
        } 
        policy.setStatus(newStatus);
        policyRepository.save(policy);
        logger.info("updatePolicyStatusByPolicyNo:Policy status updated successfully for policy number: {}", policyNo);
        return "Policy status updated successfully";
    }
	
	@Override
	public Page<InsurancePolicyDto> getInsurancePoliciesByCustomerId(int customerId, int pageNumber, int pageSize) {
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("policyNo").descending());
	    Page<InsurancePolicy> policiesPage = policyRepository.findByCustomerCustomerId(customerId, pageable);
	    
	    List<InsurancePolicyDto> policyDtos = new ArrayList<>();
	    for (InsurancePolicy policy : policiesPage.getContent()) {
	        InsurancePolicyDto dto = new InsurancePolicyDto();
	        dto.setPolicyid(policy.getPolicyNo());
	        dto.setIssueDate(policy.getIssueDate());
	        dto.setMaturityDate(policy.getMaturityDate());
	        dto.setPremiumType(policy.getPremiumType());
	        dto.setInvestAmount(policy.getInvestamount());
	        dto.setSumAssured(policy.getSumAssured());
	        dto.setPremiumAmount(policy.getPremiumamount());
	        dto.setSumAssured(policy.getSumAssured());
	        dto.setTotalInstallment(policy.getTotalinstallment());
	        dto.setStatus(policy.getStatus());
	        if(policy.getAgent()!=null) dto.setAgent(policy.getAgent().getAgentId());

	        List<Nominee> nominees = policy.getNominees();
	        List<NomineeDto> nomineeDtos = new ArrayList<>();
	        for (Nominee nominee : nominees) {
	            NomineeDto nomineeDto = new NomineeDto();
	            nomineeDto.setNomineeName(nominee.getNomineeName());
	            nomineeDto.setNomineeRelation(nominee.getNomineeRelation());
	            nomineeDtos.add(nomineeDto);
	        }
	        dto.setNominees(nomineeDtos);

	        InsuranceScheme scheme = policy.getInsuranceScheme();
	        if (scheme != null) {
	            InsurenceSchemeDto schemeDto = new InsurenceSchemeDto();
	            schemeDto.setSchemeId(scheme.getSchemeId());
	            schemeDto.setSchemeName(scheme.getSchemeName());
	            SchemeDetails isd = new SchemeDetails();
	            isd.setInstallmentCommission(scheme.getSchemeDetails().getInstallmentCommission());
	            isd.setRegistrationCommission(scheme.getSchemeDetails().getRegistrationCommission());
	            schemeDto.setSchemeDetails(isd);
	            dto.setInsuranceScheme(schemeDto);
	        }

	        policyDtos.add(dto);
	    }

	    logger.info("getInsurancePoliciesByCustomerId:Fetched {} insurance policies for customer ID: {}", policiesPage.getTotalElements(), customerId);

	    return new PageImpl<>(policyDtos, pageable, policiesPage.getTotalElements());
	}
	
	@Override
	public Page<InsurancePolicyDto> getInsurancePoliciesByCustomerIdAndDateRange(int customerId, LocalDate startDate,
			LocalDate endDate, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("policyNo").descending());
		Page<InsurancePolicy> policiesPage = policyRepository.findByCustomerCustomerIdAndIssueDateBetween(customerId,
				startDate, endDate, pageable);

		List<InsurancePolicyDto> policyDtos = new ArrayList<>();
		for (InsurancePolicy policy : policiesPage.getContent()) {
			InsurancePolicyDto dto = new InsurancePolicyDto();
			dto.setPolicyid(policy.getPolicyNo());
			dto.setIssueDate(policy.getIssueDate());
			dto.setMaturityDate(policy.getMaturityDate());
			dto.setPremiumType(policy.getPremiumType());
			dto.setInvestAmount(policy.getInvestamount());
			dto.setSumAssured(policy.getSumAssured());
			dto.setPremiumAmount(policy.getPremiumamount());
			dto.setSumAssured(policy.getSumAssured());
			dto.setTotalInstallment(policy.getTotalinstallment());
			dto.setStatus(policy.getStatus());
			if (policy.getAgent() != null)
				dto.setAgent(policy.getAgent().getAgentId());

			List<Nominee> nominees = policy.getNominees();
			List<NomineeDto> nomineeDtos = new ArrayList<>();
			for (Nominee nominee : nominees) {
				NomineeDto nomineeDto = new NomineeDto();
				nomineeDto.setNomineeName(nominee.getNomineeName());
				nomineeDto.setNomineeRelation(nominee.getNomineeRelation());
				nomineeDtos.add(nomineeDto);
			}
			dto.setNominees(nomineeDtos);

			InsuranceScheme scheme = policy.getInsuranceScheme();
			if (scheme != null) {
				InsurenceSchemeDto schemeDto = new InsurenceSchemeDto();
				schemeDto.setSchemeId(scheme.getSchemeId());
				schemeDto.setSchemeName(scheme.getSchemeName());
				SchemeDetails isd = new SchemeDetails();
				isd.setInstallmentCommission(scheme.getSchemeDetails().getInstallmentCommission());
				isd.setRegistrationCommission(scheme.getSchemeDetails().getRegistrationCommission());
				schemeDto.setSchemeDetails(isd);
				dto.setInsuranceScheme(schemeDto);
			}

			policyDtos.add(dto);
		}

		logger.info(
				"getInsurancePoliciesByCustomerIdAndDateRange: Fetched {} insurance policies for customer ID: {} within date range {} to {}",
				policiesPage.getTotalElements(), customerId, startDate, endDate);

		return new PageImpl<>(policyDtos, pageable, policiesPage.getTotalElements());
	}
	
}
