package com.example.springsocial.controller;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.springsocial.model.Company;
import com.example.springsocial.tools.RestResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.springsocial.repository.CompanyRepository;

@SuppressWarnings({"rawtypes","unchecked"})
@RestController
@RequestMapping("company")
public class CompanyController   {
	@Autowired
	private CompanyRepository repository;	    
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@GetMapping("list")
    public RestResponse list() {
		RestResponse response = new RestResponse();
		try {
			List<Company> companyList= repository.findAll();
			JSONArray array = new JSONArray();
			for (Company company : companyList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", company.getId().toString());
				jsonObject.put("name",company.getName());
				array.put(jsonObject);
			}
			JSONObject jsonData = new JSONObject();
			jsonData.put("data", array);

			response.setData(array.toString());
		}catch(Exception ex) {
			response.setError(ex.getMessage());
		}
		return response;
    }
}

