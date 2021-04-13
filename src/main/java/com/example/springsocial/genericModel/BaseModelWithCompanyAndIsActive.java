package com.example.springsocial.genericModel;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseModelWithCompanyAndIsActive extends BaseModelWithCompany {
	
	private Boolean isActive=true;	
	public Boolean getIsActive() { 		return isActive;	}
	public void setIsActive(Boolean isActive) { 		this.isActive = isActive;	}
}
