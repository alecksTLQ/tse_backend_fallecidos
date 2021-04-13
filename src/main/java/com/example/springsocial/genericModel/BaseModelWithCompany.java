package com.example.springsocial.genericModel;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.example.springsocial.model.Company;
import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
public class BaseModelWithCompany extends BaseModel {
	@ManyToOne
    @JoinColumn(name="company_id", insertable=false, updatable=false)
    private Company company; 
    
	@Column(name = "company_id")
    @JsonProperty("company_id")
	private Long companyId;
	
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
