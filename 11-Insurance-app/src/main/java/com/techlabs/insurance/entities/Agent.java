package com.techlabs.insurance.entities;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.techlabs.insurance.repository.InsurencePolicyRepository;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "agent")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Agent {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agentid")
    private int agentId;
	@Column(name = "firstname")
    private String firstname;
	@Column(name = "lastname")
    private String lastname;
	@Column(name = "qualification")
    private String qualification;
	@Column(name = "commissionearn")
    private Double commissionEarn;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
    private Status status;

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "agent_id")
    private List<InsurancePolicy> insurancePolicies;
    
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
    private List<Withdraw> withdraws;
}
