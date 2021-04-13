package com.example.springsocial.model;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.example.springsocial.genericModel.BaseModelWithCompanyAndNameAndIsActive;

@Entity
@Table(name = "users")
@SequenceGenerator(name = "default_gen", sequenceName = "users_sequence", allocationSize = 1)
public class User extends BaseModelWithCompanyAndNameAndIsActive {
	private Long userId;
	public Long getUserId() { return userId;}
	public void setUserId(Long userId) { this.userId = userId; }
}