package com.example.springsocial.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.generic.CrudController;
import com.example.springsocial.model.CabeceraFolioModell;
import com.example.springsocial.model.DetalleFolio;
import com.example.springsocial.model.IdCabecera;
import com.example.springsocial.process.CabeceraFolioProcess;
import com.example.springsocial.repository.CabeceraFolioRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.RestResponse;


@SuppressWarnings({"rawtypes", "unchecked","unused"})
@RestController
@RequestMapping("EmisionesDpi")
public class CaptacionController <T> implements CrudController{
	
	@Autowired
	CabeceraFolioRepository rp;
	
	RestResponse response = new RestResponse();
	ObjectSetGet error= new ObjectSetGet();
	IdCabecera mdl = new IdCabecera();
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("createCaptacion")
	public RestResponse createCaptacion
	(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody  CabeceraFolioModell mdlfolio)
	{
		String authTokenHeader = request.getHeader("Authorization");
		
		try {
			
			rp.save(mdlfolio);
			
		}catch(Exception exception) {
			CustomException customExcepction=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(exception); 
			response.setError(customExcepction);
		}
		
		return response;
	}

}
