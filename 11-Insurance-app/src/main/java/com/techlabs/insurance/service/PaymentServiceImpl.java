package com.techlabs.insurance.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.dto.PaymentDto;
import com.techlabs.insurance.entities.InsurancePolicy;
import com.techlabs.insurance.entities.Payment;
import com.techlabs.insurance.exceptions.PolicyNotFoundException;
import com.techlabs.insurance.repository.InsurencePolicyRepository;
import com.techlabs.insurance.repository.PaymentRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService{
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private InsurencePolicyRepository insurencePolicyRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
	
	 @Override
	    public String addPayment(PaymentDto paymentDto) {
	        Payment payment = new Payment();
	        InsurancePolicy insurancePolicy = insurencePolicyRepository.findById(paymentDto.getPolicyId()).orElse(null);
	        logger.info("Payment for policy ID {} initiated.", paymentDto.getPolicyId());

	        payment.setDate(paymentDto.getDate());
	        payment.setPaymentType(paymentDto.getPaymentType());
	        payment.setAmount(paymentDto.getAmount());
	        payment.setTax(paymentDto.getTax());
	        payment.setTotalPayment(paymentDto.getTotalPayment());
	        payment.setInsurancePolicy(insurancePolicy);

	        paymentRepository.save(payment);
	        logger.info("addPayment:Payment for policy ID {} completed successfully.", paymentDto.getPolicyId());
	        return "Payment done successfully";
	    }

	    @Override
	    public Page<PaymentDto> getPaymentsbyPolicyid(int policyid, int pageno, int pagesize) {
	        InsurancePolicy insurancePolicy = insurencePolicyRepository.findById(policyid).orElse(null);
	        if (insurancePolicy == null)
	            throw new PolicyNotFoundException("No policy found with id: " + policyid);

	        Sort sortByDateDesc = Sort.by(Sort.Direction.DESC, "paymentId");
	        Pageable pageable = PageRequest.of(pageno, pagesize, sortByDateDesc);
	        Page<Payment> payments = paymentRepository.findByInsurancePolicy(insurancePolicy, pageable);
	        Page<PaymentDto> paymentsDto = payments.map((payment) -> {
	            PaymentDto paymentDto = new PaymentDto();
	            paymentDto.setPaymentType(payment.getPaymentType());
	            paymentDto.setAmount(payment.getAmount());
	            paymentDto.setTax(payment.getTax());
	            paymentDto.setTotalPayment(payment.getTotalPayment());
	            paymentDto.setDate(payment.getDate());
	            return paymentDto;
	        });

	        logger.info("getPaymentsbyPolicyid:Retrieved payments for policy ID {} successfully.", policyid);
	        return paymentsDto;
	    }	
	    @Override
	    public Page<PaymentDto> getPaymentsbyPolicyid(int policyid, LocalDate startDate, LocalDate endDate, int pageno, int pagesize) {
	        InsurancePolicy insurancePolicy = insurencePolicyRepository.findById(policyid).orElse(null);
	        if (insurancePolicy == null)
	            throw new PolicyNotFoundException("No policy found with id: " + policyid);

	        Sort sortByDateDesc = Sort.by(Sort.Direction.DESC, "paymentId");
	        Pageable pageable = PageRequest.of(pageno, pagesize, sortByDateDesc);
	        Page<Payment> payments = paymentRepository.findByInsurancePolicyAndDateBetween(
	            insurancePolicy, startDate, endDate, pageable
	        );

	        Page<PaymentDto> paymentsDto = payments.map((payment) -> {
	            PaymentDto paymentDto = new PaymentDto();
	            paymentDto.setPaymentId(payment.getPaymentId());
	            paymentDto.setPolicyId(policyid);
	            paymentDto.setPaymentType(payment.getPaymentType());
	            paymentDto.setAmount(payment.getAmount());
	            paymentDto.setTax(payment.getTax());
	            paymentDto.setTotalPayment(payment.getTotalPayment());
	            paymentDto.setDate(payment.getDate());
	            return paymentDto;
	        });

	        logger.info("getPaymentsbyPolicyid: Retrieved payments for policy ID {} within the date range successfully.", policyid);
	        return paymentsDto;
	    }

}
