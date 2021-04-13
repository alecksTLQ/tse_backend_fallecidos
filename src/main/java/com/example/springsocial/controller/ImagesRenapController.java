package com.example.springsocial.controller;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.generic.CrudController;
import com.example.springsocial.model.ImagesRenapModel;
import com.example.springsocial.repository.ElementRepository;
import com.example.springsocial.repository.EntitiRepository;
import com.example.springsocial.repository.ImagesRenapRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.RestResponse;
import com.example.springsocial.tools.SearchCriteriaTools;

@SuppressWarnings({"rawtypes", "unchecked"})
@RestController
@RequestMapping("imagenesRenap")
public class ImagesRenapController <T> implements CrudController {
	@Autowired
	private ImagesRenapRepository repository;	    
	@Autowired
	private ElementRepository elementRepository;
	@Autowired
	private EntitiRepository entitiRepository;
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	private String moduleName="ImagesRenapModel";
	private SearchCriteriaTools searchCriteriaTools= new SearchCriteriaTools();


	
	@PostConstruct
	private void init() {
		crud.setRepository(this.repository);
		crud.setModelName(this.moduleName);
		crud.setEntitiRepository(this.entitiRepository);
		crud.setElementRepository(this.elementRepository);
		repositories.put(this.moduleName,this.repository);
	}	 
	@GetMapping("list/{cui}")
    @PreAuthorize("hasRole('USER')")
    public RestResponse list(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@PathVariable String cui) {
		init();
		if (!userPrincipal.hasPermissionToRoute(request))
			return new RestResponse(null,new CustomException("Acceso denegado",ErrorCode.ACCESS_DENIED, this.getClass().getSimpleName(),0));
		
		if (userPrincipal.getCode().toString().length()==0) 
			return new RestResponse(null,new CustomException("CUI incorrecto",ErrorCode.REST_VALIDATIONS, this.getClass().getSimpleName(),0));
		RestResponse response = new RestResponse();
		String authTokenHeader = request.getHeader("Authorization");
		ObjectSetGet error= new ObjectSetGet();
		
		try {
			
			ImagesRenapModel imagesRenap= repository.findByCui(cui);
			
			response.setData(imagesRenap);
			
			
		}catch(Exception exception) {
			CustomException customExcepction=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(exception); 
			response.setError(customExcepction);
		
		}
		return response;
	}
		
		
    }

	
	
