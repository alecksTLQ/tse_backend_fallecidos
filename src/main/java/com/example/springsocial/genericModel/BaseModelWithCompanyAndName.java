package com.example.springsocial.genericModel;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseModelWithCompanyAndName extends BaseModelWithCompany {
	@Column(nullable = false)
	private String name;
	public String getName() {		return name;	}
	public void setName(String name) {		this.name = name;	}
}
