package com.example.springsocial.controller;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.crud.CRUD;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.generic.CrudController;
import com.example.springsocial.model.CabeceraFolioModelN;
import com.example.springsocial.model.DetalleFolioModelN;
import com.example.springsocial.model.IdpaqueteDetalle;
import com.example.springsocial.process.CaptacionFallecidoProcess;
import com.example.springsocial.repository.CabeceraFolioRepositoryN;
import com.example.springsocial.repository.DetalleFolioRepositoryN;
import com.example.springsocial.repository.ElementRepository;
import com.example.springsocial.repository.EntitiRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;
import com.example.springsocial.tools.SearchCriteriaTools;

@SuppressWarnings({"rawtypes", "unchecked","unused"})
@RestController
@RequestMapping("EmisionesDpi")
public class CaptacionControllerN <T> implements CrudController{
	
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	private ElementRepository elementRepository;
	@Autowired
	private EntitiRepository entitiRepository;
	@Autowired
	CabeceraFolioRepositoryN rpCabeceraFolio;
	@Autowired
	DetalleFolioRepositoryN rpDetalleFolio;
	
	private CaptacionFallecidoProcess captacion = new CaptacionFallecidoProcess();
	private String moduleName="DetalleFolioModelN";
	private DateTools dateTools = new DateTools();
	private SearchCriteriaTools searchCriteriaTools= new SearchCriteriaTools();
	ObjectSetGet data = new ObjectSetGet();
	RestResponse response = new RestResponse();
	ObjectSetGet error= new ObjectSetGet();
	private CRUD crud = new CRUD();
	
	@PostConstruct
	private void init() {
		crud.setRepository(this.rpDetalleFolio);
		crud.setModelName(this.moduleName);
		crud.setEntitiRepository(this.entitiRepository);
		crud.setElementRepository(this.elementRepository);
		repositories.put(this.moduleName,this.rpDetalleFolio);
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("captacionFallecido")
	public RestResponse crearFolioyDetalle(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody Object createElement) throws CustomException {
		String authTokenHeader = request.getHeader("Authorization");
		Integer indicador = 0;
		
		try {
			data.setObject(createElement);
			if (data.getValue("nrofolio")==null || data.getValue("nrofolio")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Folio",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("fechainicio")==null || data.getValue("fechainicio")=="" ) return new RestResponse(null,new CustomException("Ingrese la fecha inicial",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("fechafinal")==null || data.getValue("fechafinal")=="" ) return new RestResponse(null,new CustomException("Ingrese la fecha final",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("coddepto")==null || data.getValue("coddepto")=="" ) return new RestResponse(null,new CustomException("Ingrese el departamento",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("codmunic")==null || data.getValue("codmunic")=="" ) return new RestResponse(null,new CustomException("Ingrese el municipio",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			
			captacion.setRepository(rpDetalleFolio, rpCabeceraFolio);
			captacion.setNroFolio(data.getValue("nrofolio").toString());
			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
			captacion.setToken(authTokenHeader);
			captacion.setData(createElement);
			indicador = captacion.save();
			
			if (captacion.getResponse().getError()!=null)throw new Exception(captacion.getResponse().getError().toString());
			else {
				if(indicador==0) {
					response.setData("FALLECIDO CAPTADO CORRECTAMENTE!");
				}else {
					response.setData("ESTAS INTENTADO GUARDAR MAS DE 10 LINEAS POR FOLIO!");
				}
			}
			
		}catch (Exception exception) {
			CustomException customExcepction= new CustomException(exception.getMessage(),ErrorCode.REST_UPDATE,this.getClass().getSimpleName(), 0);
			response.setError(customExcepction);
	    }
		return response;
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("buscarFallecido")
	public RestResponse obtener(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody Object element) throws CustomException {

		String authTokenHeader = request.getHeader("Authorization");
		ObjectSetGet data= new ObjectSetGet();
		IdpaqueteDetalle mdlid = new IdpaqueteDetalle();
		Integer año  = 0;
		try {
			data.setObject(element);
			año = dateTools.getYearOfCurrentDate();
			
			captacion.setRepository(rpDetalleFolio,rpCabeceraFolio);
			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
			captacion.setToken(authTokenHeader);
			captacion.setData(element);
			captacion.select();
			
			if (captacion.getResponse().getError()!=null)throw new Exception(captacion.getResponse().getError().toString());
			else {
				response.setData(captacion.getResponse().getData());
			}
			
		}catch (Exception exception) {
			CustomException customExcepction= new CustomException(exception.getMessage(),ErrorCode.REST_UPDATE,this.getClass().getSimpleName(), 0);
			response.setError(customExcepction);
	    }
		
		return response;
	}
	
	@GetMapping("lista")
	public RestResponse lista(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestParam("searchCriteria") Optional<String> searchCriteria,@RequestParam Optional<String> orderCriteria) {
		init();
		String authTokenHeader = request.getHeader("Authorization");
		ObjectSetGet data= new ObjectSetGet();
		
		System.out.println(userPrincipal.getCode());
		try {
			
			//response.setData(rpDetalleFolio.findAll());
			crud.setRequest(request);
			crud.setUserPrincipal(userPrincipal);
			searchCriteriaTools.clear();
			searchCriteriaTools.setSearchCriteria(searchCriteria);
			crud.list(searchCriteriaTools.getSearchCriteria() , orderCriteria);
			
			response.setData(crud.getResponse());
			
		}catch(Exception e) {
			response.setError(e.getMessage());
		}
		
		return response;
	}
	
	@PreAuthorize("hasRole('USER')")
	@PutMapping("modificarFallecido")
	public RestResponse modificar(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@RequestBody Object element) {
		String authTokenHeader = request.getHeader("Authorization");
		ObjectSetGet data= new ObjectSetGet();
		
		try {
			data.setObject(element);
			if (data.getValue("id")==null || data.getValue("id")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Folio",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			
			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
			captacion.setToken(authTokenHeader);
			captacion.setData(element);
			captacion.update();
			
			if (captacion.getResponse().getError()!=null)throw new Exception(captacion.getResponse().getError().toString());
			else {
				response.setData("REGISTRO MODIFICADO CORRECTAMENTE!");
			}
			
		}catch (Exception exception) {
			CustomException customExcepction= new CustomException(exception.getMessage(),ErrorCode.REST_UPDATE,this.getClass().getSimpleName(), 0);
			response.setError(customExcepction);
	    }
		
		return response;
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("verificarFallecido")
	public RestResponse verificarFallecidos(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@RequestBody Object element) {
		
		String authTokenHeader = request.getHeader("Authorization");
		ObjectSetGet data= new ObjectSetGet();
		
		try {
			
			data.setObject(element);
			if (data.getValue("nroboleta")==null || data.getValue("nroboleta")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Boleta",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("nroorden")==null || data.getValue("nroorden")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Orden",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("nroregistro")==null || data.getValue("nroregistro")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Registro",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("cui")==null || data.getValue("cui")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de CUI",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("iddetalle")==null || data.getValue("iddetalle")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Folio",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));

			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
			captacion.setData(element);
			captacion.setToken(authTokenHeader);
			captacion.VerificacionyActualizarPadron();
			
			if (captacion.getResponse().getError()!=null)throw new Exception(captacion.getResponse().getError().toString());
			else {
				response.setData("VERIFICACION Y ACTUALIZACION DE ESTADO EN EL PADRON CORRECTA!");
			}
			
			// SELECT * FROM TPADRON WHERE NROBOLETA =?
			// SELECT c.* , p.* from TCEDULA c, TPADRON p where c.nroboleta = p.nroboleta and c.ordenced = ? and c.registroced =?
			// SELECT d.* , p.* from TDPI d, TPADRON p where d.nroboleta = p.nroboleta and d.cui = ?			
			// UPDATE TPADRON SET status = ? , USRMOD =? , FECMOD = ? , IDPRO = 27, FECPRO = SYSDATE, PUESTOMODIFICA = 119 WHERE NROBOLETA = ?
			
			
		}catch (Exception exception) {
			CustomException customExcepction= new CustomException(exception.getMessage(),ErrorCode.REST_UPDATE,this.getClass().getSimpleName(), 0);
			response.setError(customExcepction);
	    }
		
		return response;
	}
	
}
