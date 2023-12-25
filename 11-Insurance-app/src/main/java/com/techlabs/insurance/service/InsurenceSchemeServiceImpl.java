package com.techlabs.insurance.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.dto.InsurenceSchemeDto;
import com.techlabs.insurance.entities.InsurancePlan;
import com.techlabs.insurance.entities.InsuranceScheme;
import com.techlabs.insurance.entities.SchemeDetails;
import com.techlabs.insurance.entities.Status;
import com.techlabs.insurance.exceptions.AccountNotFoundException;
import com.techlabs.insurance.exceptions.InsurancePlanNotFoundException;
import com.techlabs.insurance.exceptions.InsuranceSchemeNotFound;
import com.techlabs.insurance.repository.InsurencePlanRepository;
import com.techlabs.insurance.repository.InsurenceSchemeRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class InsurenceSchemeServiceImpl implements InsurenceSchemeService {
	private final Logger logger = LoggerFactory.getLogger(InsurenceSchemeServiceImpl.class);

	@Autowired
	private InsurenceSchemeRepository insurenceSchemeRepo;

	@Autowired
	private InsurencePlanRepository insurancePlanRepo;

	@Value("${upload.folder.path}")
	private String uploadFolder;

	@Override
	public void addInsurenceScheme(InsurenceSchemeDto insuranceSchemeDto) {
		try {
			MultipartFile imageFile = insuranceSchemeDto.getImage();
			String fileName = imageFile.getOriginalFilename();
			String filePath = uploadFolder + File.separator + fileName;

			imageFile.transferTo(new File(filePath));

			SchemeDetails schemeDetails = new SchemeDetails();
			schemeDetails.setImage(readImageAsByteArray(filePath));
			schemeDetails.setDescription(insuranceSchemeDto.getSchemeDetails().getDescription());
			schemeDetails.setMinAmount(insuranceSchemeDto.getSchemeDetails().getMinAmount());
			schemeDetails.setMaxAmount(insuranceSchemeDto.getSchemeDetails().getMaxAmount());
			schemeDetails.setMinInvestment(insuranceSchemeDto.getSchemeDetails().getMinInvestment());
			schemeDetails.setMaxInvestment(insuranceSchemeDto.getSchemeDetails().getMaxInvestment());
			schemeDetails.setMinAge(insuranceSchemeDto.getSchemeDetails().getMinAge());
			schemeDetails.setMaxAge(insuranceSchemeDto.getSchemeDetails().getMaxAge());
			schemeDetails.setProfitRatio(insuranceSchemeDto.getSchemeDetails().getProfitRatio());
			schemeDetails.setRegistrationCommission(insuranceSchemeDto.getSchemeDetails().getRegistrationCommission());
			schemeDetails.setInstallmentCommission(insuranceSchemeDto.getSchemeDetails().getInstallmentCommission());

			InsuranceScheme insuranceScheme = new InsuranceScheme();
			insuranceScheme.setSchemeName(insuranceSchemeDto.getSchemeName());
			insuranceScheme.setSchemeDetails(schemeDetails);
			insuranceScheme.setStatus(Status.ACTIVE);

			InsurancePlan insurancePlan = insurancePlanRepo.findById(insuranceSchemeDto.getPlanId()).orElse(null);
			if (insurancePlan == null) {
				throw new InsurancePlanNotFoundException("Plan not found! Select a correct plan");
			}
			insuranceScheme.setInsurancePlan(insurancePlan);

			insurenceSchemeRepo.save(insuranceScheme);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] readImageAsByteArray(String imagePath) throws IOException {
		Path path = Paths.get(imagePath);
		return Files.readAllBytes(path);
	}

	@Override
	public List<InsurenceSchemeDto> getAllInsurenceSchemes(String keyword) {
		List<InsuranceScheme> schemes = insurenceSchemeRepo.findByschemeNameContaining(keyword);
		List<InsurenceSchemeDto> schemeDtos = new ArrayList<>();

		for (InsuranceScheme scheme : schemes) {
			InsurenceSchemeDto schemeDto = new InsurenceSchemeDto();
			schemeDto.setSchemeId(scheme.getSchemeId());
			schemeDto.setSchemeName(scheme.getSchemeName());
			schemeDto.setPlanId(scheme.getInsurancePlan().getPlanId());
			SchemeDetails decompressedSchemeDetails = new SchemeDetails();

			SchemeDetails compressedSchemeDetails = scheme.getSchemeDetails();
			if (compressedSchemeDetails.getImage() != null) {
				decompressedSchemeDetails.setImage(compressedSchemeDetails.getImage());
				decompressedSchemeDetails.setDescription(compressedSchemeDetails.getDescription());
				decompressedSchemeDetails.setMinAmount(compressedSchemeDetails.getMinAmount());
				decompressedSchemeDetails.setMaxAmount(compressedSchemeDetails.getMaxAmount());
				decompressedSchemeDetails.setMinInvestment(compressedSchemeDetails.getMinInvestment());
				decompressedSchemeDetails.setMaxInvestment(compressedSchemeDetails.getMaxInvestment());
				decompressedSchemeDetails.setMinAge(compressedSchemeDetails.getMinAge());
				decompressedSchemeDetails.setMaxAge(compressedSchemeDetails.getMaxAge());
				decompressedSchemeDetails.setProfitRatio(compressedSchemeDetails.getProfitRatio());
				decompressedSchemeDetails
						.setRegistrationCommission(compressedSchemeDetails.getRegistrationCommission());
				decompressedSchemeDetails.setInstallmentCommission(compressedSchemeDetails.getInstallmentCommission());

				schemeDto.setSchemeDetails(decompressedSchemeDetails);
			} else {
				System.out.println("This image was null");
			}

			schemeDto.setStatus(scheme.getStatus());
			schemeDtos.add(schemeDto);
		}

		logger.info(" getAllInsurenceSchemes:Retrieved all insurance schemes successfully.");
		return schemeDtos;
	}

	@Override
	public void updateInsurenceScheme(int schemeId, InsurenceSchemeDto insuranceSchemeDto) {
		InsuranceScheme existingScheme = insurenceSchemeRepo.findById(schemeId)
				.orElseThrow(() -> new IllegalArgumentException("Scheme not found with ID: " + schemeId));

		existingScheme.setSchemeName(insuranceSchemeDto.getSchemeName());
		existingScheme.setSchemeDetails(insuranceSchemeDto.getSchemeDetails());
		existingScheme.setStatus(insuranceSchemeDto.getStatus());

		insurenceSchemeRepo.save(existingScheme);
		logger.info("Updated insurance scheme with ID: {} successfully.", schemeId);
	}

	@Override
	public void deleteInsurenceSchemeById(int id) {
		insurenceSchemeRepo.deleteById(id);
		logger.info("updateInsurenceScheme: Deleted insurance scheme with ID: {} successfully.", id);
	}
//	@Override
//	public List<InsurenceSchemeDto> getActiveInsurenceSchemes() {
//		List<InsuranceScheme> activeSchemes = insurenceSchemeRepo.findByStatus(Status.ACTIVE);
//		List<InsurenceSchemeDto> activeSchemeDtos = new ArrayList<>();
//
//		for (InsuranceScheme scheme : activeSchemes) {
//			InsurenceSchemeDto schemeDto = new InsurenceSchemeDto();
//			schemeDto.setSchemeId(scheme.getSchemeId());
//			schemeDto.setSchemeName(scheme.getSchemeName());
//			schemeDto.setSchemeDetails(scheme.getSchemeDetails());
//			schemeDto.setStatus(scheme.getStatus());
//
//			activeSchemeDtos.add(schemeDto);
//		}
//
//		return activeSchemeDtos;
//	}

	@Override
	public List<InsurenceSchemeDto> getActiveSchemesandPlans() {
		List<InsuranceScheme> activeSchemes = insurenceSchemeRepo.findByStatus(Status.ACTIVE);
		List<InsurenceSchemeDto> activeSchemeDtos = new ArrayList<>();

		for (InsuranceScheme scheme : activeSchemes) {
			InsurancePlan insurancePlan = scheme.getInsurancePlan();
			if (insurancePlan != null && insurancePlan.getStatus() == Status.ACTIVE) {
				InsurenceSchemeDto schemeDto = new InsurenceSchemeDto();
				schemeDto.setSchemeId(scheme.getSchemeId());
				schemeDto.setSchemeName(scheme.getSchemeName());
				schemeDto.setSchemeDetails(scheme.getSchemeDetails());
				schemeDto.setStatus(scheme.getStatus());
				schemeDto.setPlanId(scheme.getInsurancePlan().getPlanId());
				activeSchemeDtos.add(schemeDto);
			}
		}

		logger.info("getActiveSchemesandPlans: Retrieved active schemes and plans successfully.");
		return activeSchemeDtos;
	}

	@Override
	public List<InsurenceSchemeDto> getActiveSchemesByPlanid(int planid) {
		List<InsuranceScheme> activeSchemes = insurenceSchemeRepo.findByStatus(Status.ACTIVE);
		List<InsurenceSchemeDto> activeSchemeDtos = new ArrayList<>();

		for (InsuranceScheme scheme : activeSchemes) {
			InsurancePlan insurancePlan = scheme.getInsurancePlan();
			if (insurancePlan.getPlanId() == 2)
				System.out.println(insurancePlan.getStatus());
			if (insurancePlan != null && insurancePlan.getPlanId() == planid
					&& insurancePlan.getStatus() == Status.ACTIVE) {
				InsurenceSchemeDto schemeDto = new InsurenceSchemeDto();
				schemeDto.setSchemeId(scheme.getSchemeId());
				schemeDto.setSchemeName(scheme.getSchemeName());
				schemeDto.setSchemeDetails(scheme.getSchemeDetails());
				schemeDto.setStatus(scheme.getStatus());
				schemeDto.setPlanId(scheme.getInsurancePlan().getPlanId());
				activeSchemeDtos.add(schemeDto);
			}
		}

		logger.info("getActiveSchemesByPlanid: Retrieved active schemes by plan ID successfully.");
		return activeSchemeDtos;
	}

	@Override
	public InsuranceScheme getInsuranceSchemeById(int schemeId) {
		return insurenceSchemeRepo.findById(schemeId)
				.orElseThrow(() -> new AccountNotFoundException("Insurance scheme with ID " + schemeId + " not found"));
	}

	@Override
	public Page<InsurenceSchemeDto> getAllInsurenceSchemesPageWise(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<InsuranceScheme> schemesPage = insurenceSchemeRepo.findAll(pageable);

		logger.info("Retrieved insurance schemes page {} of size {} successfully.", pageNumber, pageSize);
		return schemesPage.map(scheme -> {
			InsurenceSchemeDto schemeDto = new InsurenceSchemeDto();
			schemeDto.setSchemeId(scheme.getSchemeId());
			schemeDto.setSchemeName(scheme.getSchemeName());
			schemeDto.setPlanId(scheme.getInsurancePlan().getPlanId());

			SchemeDetails decompressedSchemeDetails = new SchemeDetails();

			SchemeDetails compressedSchemeDetails = scheme.getSchemeDetails();
			if (compressedSchemeDetails.getImage() != null) {
				decompressedSchemeDetails.setImage(compressedSchemeDetails.getImage());
				decompressedSchemeDetails.setDescription(compressedSchemeDetails.getDescription());
				decompressedSchemeDetails.setMinAmount(compressedSchemeDetails.getMinAmount());
				decompressedSchemeDetails.setMaxAmount(compressedSchemeDetails.getMaxAmount());
				decompressedSchemeDetails.setMinInvestment(compressedSchemeDetails.getMinInvestment());
				decompressedSchemeDetails.setMaxInvestment(compressedSchemeDetails.getMaxInvestment());
				decompressedSchemeDetails.setMinAge(compressedSchemeDetails.getMinAge());
				decompressedSchemeDetails.setMaxAge(compressedSchemeDetails.getMaxAge());
				decompressedSchemeDetails.setProfitRatio(compressedSchemeDetails.getProfitRatio());
				decompressedSchemeDetails
						.setRegistrationCommission(compressedSchemeDetails.getRegistrationCommission());
				decompressedSchemeDetails.setInstallmentCommission(compressedSchemeDetails.getInstallmentCommission());

				schemeDto.setSchemeDetails(decompressedSchemeDetails);
			} else {
				System.out.println("This image was null");
			}

			schemeDto.setStatus(scheme.getStatus());
			return schemeDto;
		});
	}

	@Override
	public Page<InsurenceSchemeDto> getInsurenceSchemesByStatus(Status status, int pageno, int pagesize) {
		Pageable pageable = PageRequest.of(pageno, pagesize);
		Page<InsuranceScheme> schemes = insurenceSchemeRepo.findByStatus(status, pageable);

		Page<InsurenceSchemeDto> schemeDtos = schemes.map((scheme) -> {
			InsurenceSchemeDto schemeDto = new InsurenceSchemeDto();
			schemeDto.setSchemeId(scheme.getSchemeId());
			schemeDto.setSchemeName(scheme.getSchemeName());
			schemeDto.setPlanId(scheme.getInsurancePlan().getPlanId());

			SchemeDetails decompressedSchemeDetails = new SchemeDetails();
			SchemeDetails compressedSchemeDetails = scheme.getSchemeDetails();
			if (compressedSchemeDetails.getImage() != null) {
				decompressedSchemeDetails.setImage(compressedSchemeDetails.getImage());
				decompressedSchemeDetails.setDescription(compressedSchemeDetails.getDescription());
				decompressedSchemeDetails.setMinAmount(compressedSchemeDetails.getMinAmount());
				decompressedSchemeDetails.setMaxAmount(compressedSchemeDetails.getMaxAmount());
				decompressedSchemeDetails.setMinInvestment(compressedSchemeDetails.getMinInvestment());
				decompressedSchemeDetails.setMaxInvestment(compressedSchemeDetails.getMaxInvestment());
				decompressedSchemeDetails.setMinAge(compressedSchemeDetails.getMinAge());
				decompressedSchemeDetails.setMaxAge(compressedSchemeDetails.getMaxAge());
				decompressedSchemeDetails.setProfitRatio(compressedSchemeDetails.getProfitRatio());
				decompressedSchemeDetails
						.setRegistrationCommission(compressedSchemeDetails.getRegistrationCommission());
				decompressedSchemeDetails.setInstallmentCommission(compressedSchemeDetails.getInstallmentCommission());

				schemeDto.setSchemeDetails(decompressedSchemeDetails);
			} else {
				System.out.println("This image was null");
			}

			schemeDto.setStatus(scheme.getStatus());
			return schemeDto;
		});

		logger.info("getInsurenceSchemesByStatus: Retrieved insurance schemes by status successfully.");
		return schemeDtos;
	}

	@Override
	public void updateInsurenceSchemeStatus(int schemeId, Status newStatus) {
		InsuranceScheme insuranceScheme = insurenceSchemeRepo.findById(schemeId)
				.orElseThrow(() -> new InsuranceSchemeNotFound("Insurance scheme with ID " + schemeId + " not found"));
		insuranceScheme.setStatus(newStatus);
		insurenceSchemeRepo.save(insuranceScheme);

		logger.info(
				"updateInsurenceSchemeStatus: Updated insurance scheme status successfully. Scheme ID: {}, New Status: {}",
				schemeId, newStatus);
	}

}
