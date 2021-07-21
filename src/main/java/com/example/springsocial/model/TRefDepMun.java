package com.example.springsocial.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name="TREFDEPMUN")
public class TRefDepMun implements Serializable{
	
	@Column(name="CODDEP")
	private Integer CODDEP;
	

}
