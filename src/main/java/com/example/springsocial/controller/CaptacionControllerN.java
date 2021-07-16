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
import com.example.springsocial.model.DetalleFolioHistoricoModelN;
import com.example.springsocial.model.DetalleFolioModelN;
import com.example.springsocial.model.IdpaqueteDetalle;
import com.example.springsocial.process.CaptacionFallecidoProcess;
import com.example.springsocial.repository.CabeceraFolioRepositoryN;
import com.example.springsocial.repository.DetalleFolioHistoricoRepository;
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
	@Autowired
	DetalleFolioHistoricoRepository rpDetalleFolioHistorico;
	
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
			
			captacion.setRepository(rpDetalleFolio, rpCabeceraFolio, rpDetalleFolioHistorico);
			captacion.setNroFolio(data.getValue("nrofolio").toString());
			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
			captacion.setToken(authTokenHeader);
			captacion.setData(createElement);
			indicador = captacion.save();
			//12016	10/11/83	MANUEL	DE JESUS	CHILEL	GALVES		9	19/09/60	12	21	1	3	0	1	1	169	49	843	12 CALLE MIRAFLORES	17-31	11	1	1	999			JASANTIAGO	16/02/15			37	03/11/14	2	119	21-05	
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
	public RestResponse Buscar(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody Object element) throws CustomException {

		String authTokenHeader = request.getHeader("Authorization");
		ObjectSetGet data= new ObjectSetGet();
		IdpaqueteDetalle mdlid = new IdpaqueteDetalle();
		Integer año  = 0;
		try {
			data.setObject(element);
			año = dateTools.getYearOfCurrentDate();
			
			captacion.setRepository(rpDetalleFolio,rpCabeceraFolio, rpDetalleFolioHistorico);
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
	
	@PreAuthorize("hasRole('USER')")
	@PutMapping("modificarFallecido")
	public RestResponse modificar(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@RequestBody Object element) throws CustomException {
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
	public RestResponse verificarFallecidos(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@RequestBody Object element) throws CustomException {
		Integer valor = null;
		String authTokenHeader = request.getHeader("Authorization");
		ObjectSetGet data= new ObjectSetGet();
		
		try {
			
			data.setObject(element);
			if (data.getValue("nroboleta")==null || data.getValue("nroboleta")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Boleta",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("orden")==null || data.getValue("orden")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Orden",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("registro")==null || data.getValue("registro")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Registro",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("dpi")==null || data.getValue("dpi")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de CUI",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("iddetalle")==null || data.getValue("iddetalle")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Folio",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));

			captacion.setRepository(rpDetalleFolio,rpCabeceraFolio, rpDetalleFolioHistorico);
			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
			captacion.setData(element);
			captacion.setToken(authTokenHeader);
			valor = captacion.VerificacionyActualizarPadron();
			
			if (captacion.getResponse().getError()!=null)throw new Exception(captacion.getResponse().getError().toString());
			else {
				if(valor==0) {
					response.setData("VERIFICACION, ALMACENADO EN HISTORICO Y ACTUALIZACION DE ESTADO EN EL PADRON CORRECTA!");
				}else {
					response.setData("VERIFICACION Y ALMACENADO EN HISTORICO CORRECTO!");
				}
			}
			
		}catch (Exception exception) {
			CustomException customExcepction= new CustomException(exception.getMessage(),ErrorCode.REST_UPDATE,this.getClass().getSimpleName(), 0);
			response.setError(customExcepction);
	    }
		
		return response;
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("CorreccionFallecido")
	public RestResponse CorreccionFallecido(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@RequestBody Object element) throws CustomException {
		
		String authTokenHeader = request.getHeader("Authorization");
		
		try {
		
			data.setObject(element);
			if (data.getValue("iddetalle")==null || data.getValue("iddetalle")=="" ) return new RestResponse(null,new CustomException("iddetalle",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			
			captacion.setRepository(rpDetalleFolio, rpCabeceraFolio, rpDetalleFolioHistorico);
			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
			captacion.setData(element);
			captacion.setToken(authTokenHeader);
			captacion.ProcesoCorrecccion();
			
			if (captacion.getResponse().getError()!=null)throw new Exception(captacion.getResponse().getError().toString());
			else {
				
				response.setData("RECUPERACION DE HISTORICO CORRECTA!");
			}
			
		}catch (Exception exception) {
			CustomException customExcepction= new CustomException(exception.getMessage(),ErrorCode.REST_UPDATE,this.getClass().getSimpleName(), 0);
			response.setError(customExcepction);
	    }
		
		return response;
	}
}
