package com.example.springsocial.controller;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.springsocial.crud.CRUD;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.generic.CrudController;
import com.example.springsocial.model.NoFolioModel;
import com.example.springsocial.process.NoFolioProcess;
import com.example.springsocial.repository.ElementRepository;
import com.example.springsocial.repository.EntitiRepository;
import com.example.springsocial.repository.NoFolioRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;
import com.example.springsocial.transaction.NoFolioTransaction;

//@SuppressWarnings({"rawtypes", "unchecked","unused"})
//@RestController
//@RequestMapping("Defunciones")
public class NoFolioController <T> implements CrudController {

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	NoFolioRepository repository;
	
	private NoFolioProcess folioprocess = new NoFolioProcess();
	RestResponse response = new RestResponse();
	ObjectSetGet error= new ObjectSetGet();
	
	/* METODOS  */
	@PreAuthorize("hasRole('USER')")
	@PostMapping("createFolio/{usuario}/{cantidad}")
	public RestResponse createFolio(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @PathVariable String usuario,@PathVariable Integer cantidad) {
		//if (!userPrincipal.hasPermissionToRoute(request))
			//return new RestResponse(null,new CustomException("Acceso denegado",ErrorCode.ACCESS_DENIED, this.getClass().getSimpleName(),0));
		
		String authTokenHeader = request.getHeader("Authorization");
		
		try {
			
			if(usuario==null || usuario=="") 
				return new RestResponse(null, new CustomException("Debe completar Campo Usuario", ErrorCode.REST_CREATE, this.getClass().getSimpleName(),0));
			if(cantidad==null) 
				return new RestResponse(null, new CustomException("Debe completar Campo Cantidad", ErrorCode.REST_CREATE, this.getClass().getSimpleName(),0));
			
			folioprocess.setEntityManagerFactory(entityManagerFactory);
			response = folioprocess.create(usuario, cantidad);
			
		}catch(Exception exception) {
			CustomException customExcepction=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(exception); 
			response.setError(customExcepction);
		}
		
		
		return response;
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("obtenerFolio/{usuario}")
	public RestResponse obtenerFolioLinea(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @PathVariable String usuario) {
		/* OBTENCION DE NO. FOLIO POR USUARIO Y AÑO ACTUAL */
		String authTokenHeader = request.getHeader("Authorization");
		
		try {
			if(usuario==null || usuario=="") 
				return new RestResponse(null, new CustomException("Usuario no obtenido", ErrorCode.REST_CREATE, this.getClass().getSimpleName(),0));
			
			folioprocess.setEntityManagerFactory(entityManagerFactory);
			response.setData(folioprocess.obtener(usuario));
			
		}catch(Exception exception) {
			CustomException customExcepction=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(exception); 
			response.setError(customExcepction);
		}
		
		return response;
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("obtenerFolios")
	public RestResponse obtenerFolios(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request) {
		/* OBTENCION DE FOLIOS SEGUN AÑO ACTUAL */
		String authTokenHeader = request.getHeader("Authorization");
		try {
			
			folioprocess.setEntityManagerFactory(entityManagerFactory);
			response = folioprocess.obtenerRegistrosFolios();
			
		}catch(Exception exception) {
			CustomException customExcepction=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(exception); 
			response.setError(customExcepction);
		}
		
		return response;
	}
	
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("EliminarFolio/{usuario}/{folio}")
	public RestResponse EliminarFolio(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @PathVariable String usuario, @PathVariable Integer folio) {
		String authTokenHeader = request.getHeader("Authorization");
		
		try {
			if(usuario==null || usuario=="") 
				return new RestResponse(null, new CustomException("Falta definicion del campo usuario", ErrorCode.REST_CREATE, this.getClass().getSimpleName(),0));
			if(folio==null) 
				return new RestResponse(null, new CustomException("Falta definicion del campo folio", ErrorCode.REST_CREATE, this.getClass().getSimpleName(),0));
			
			folioprocess.setEntityManagerFactory(entityManagerFactory);
			response.setData(folioprocess.EliminarFolio(usuario, folio));
			
		}catch(Exception exception) {
			CustomException customExcepction=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(exception);
			response.setError(customExcepction);
		}
		
		return response;
	}
	
	/*@GetMapping("id")
	public RestResponse id() {
		
		
		try {
			
			NoFolioModel objeto = new NoFolioModel();
			objeto.setAño(2021);
			objeto.setEstado(1);
			objeto.setNrolinea(1);
			objeto.setUsuario("hello");
			//objeto.setNrofolio(repository.selectNoFolio());
			repository.save(objeto);
			response.setData(objeto.getNrofolio());
			
		}catch(Exception e) {
			
		}
		
		
		return response;
	}*/
	
	
}
