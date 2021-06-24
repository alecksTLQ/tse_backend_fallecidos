package com.example.springsocial.controller;

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
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;

@SuppressWarnings({"rawtypes", "unchecked","unused"})
@RestController
@RequestMapping("EmisionesDpi")
public class CaptacionControllerN <T> implements CrudController{
	
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	CabeceraFolioRepositoryN rpCabeceraFolio;
	@Autowired
	DetalleFolioRepositoryN rpDetalleFolio;
	
	private CaptacionFallecidoProcess captacion = new CaptacionFallecidoProcess();
	
	private DateTools dateTools = new DateTools();
	ObjectSetGet data = new ObjectSetGet();
	RestResponse response = new RestResponse();
	ObjectSetGet error= new ObjectSetGet();
	private CRUD crud = new CRUD();
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("captacion")
	public RestResponse crearFolioyDetalle(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody Object createElement) throws CustomException {
		
		Integer indicador = 0;
		
		try {
			data.setObject(createElement);
			if (data.getValue("idpaquete")==null || data.getValue("idpaquete")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Folio",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
						
			captacion.setNroFolio(data.getValue("idpaquete").toString());
			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
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
	@PostMapping("buscar")
	public RestResponse obtener(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody Object element) throws CustomException {

		String authTokenHeader = request.getHeader("Authorization");
		ObjectSetGet data= new ObjectSetGet();
		IdpaqueteDetalle mdlid = new IdpaqueteDetalle();
		Integer año  = 0;
		try {
			data.setObject(element);
			año = dateTools.getYearOfCurrentDate();
			
			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
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
	@PutMapping("modificar")
	public RestResponse modificar(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@RequestBody Object element) {
		String authTokenHeader = request.getHeader("Authorization");
		ObjectSetGet data= new ObjectSetGet();
		
		try {
			data.setObject(element);
			if (data.getValue("idpaquete")==null || data.getValue("idpaquete")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Folio",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			
			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
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
	@PostMapping("verificar")
	public RestResponse verificarFallecidos(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@RequestBody Object element) {
		
		String authTokenHeader = request.getHeader("Authorization");
		ObjectSetGet data= new ObjectSetGet();
		
		try {
			
			data.setObject(element);
			if (data.getValue("nroboleta")==null || data.getValue("nroboleta")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Boleta",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("nroorden")==null || data.getValue("nroorden")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Orden",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("nroregistro")==null || data.getValue("nroregistro")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Registro",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("cui")==null || data.getValue("cui")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de CUI",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("idpaquete")==null || data.getValue("idpaquete")=="" ) return new RestResponse(null,new CustomException("Ingrese el No. de Folio",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));

			captacion.setEntityManagerFactory(entityManagerFactory);
			captacion.setUserPrincipal(userPrincipal);
			captacion.setData(element);
			captacion.setToken(authTokenHeader);
			captacion.VerificacionyActualizarPadron();
			
			if (captacion.getResponse().getError()!=null)throw new Exception(captacion.getResponse().getError().toString());
			else {
				response.setData("REGISTRO!");
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
