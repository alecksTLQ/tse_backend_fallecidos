package com.example.springsocial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springsocial.repository.NoFolioRepository;


@Service
public class NoFolioService {

	@Autowired
	private NoFolioRepository rpNofolio;
	
	public Integer SelectByAño(Integer valor) {
		Integer rest = null;
		rest =  rpNofolio.findByaño(2021);
	
		return rest;
	}
	
	
}
