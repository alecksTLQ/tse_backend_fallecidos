package com.example.springsocial.genericModel;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseModelWithCompanyAndNameAndIsActive extends BaseModelWithCompany {
	@Column(nullable = false)
	private String name;
	private Boolean isActive=true;	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
