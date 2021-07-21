package com.example.springsocial.controller;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.springsocial.crud.CRUD;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.generic.CrudController;
import com.example.springsocial.model.DetalleFolioModelN;
import com.example.springsocial.process.CaptacionFallecidoProcess;
import com.example.springsocial.process.CaptacionFallecido_RenapDefuncionesProcess;
import com.example.springsocial.quartz.PlayService;
import com.example.springsocial.repository.CabeceraFolioRepositoryN;
import com.example.springsocial.repository.DetalleFolioHistoricoRepository;
import com.example.springsocial.repository.DetalleFolioRepositoryN;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;

@SuppressWarnings({"rawtypes", "unchecked","unused"})
@RestController
@RequestMapping("Defunciones")
public class ProcesoAutomaticoController <T> implements CrudController{
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	private CabeceraFolioRepositoryN rpCabeceraFolio;
	@Autowired
	private DetalleFolioRepositoryN rpDetalleFolio;
	@Autowired
	private DetalleFolioHistoricoRepository rpDetalleHistorico;
	@Autowired
	private PlayService serviceQuartz;
	
	private List<DetalleFolioModelN> listaDetalle;
	
	String authTokenHeader = null;
	
	
	private CaptacionFallecido_RenapDefuncionesProcess captacionRenap = new CaptacionFallecido_RenapDefuncionesProcess();
	
	private DateTools dateTools = new DateTools();
	ObjectSetGet data = new ObjectSetGet();
	RestResponse response = new RestResponse();
	ObjectSetGet error= new ObjectSetGet();
	private CRUD crud = new CRUD();
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("obtenerRegistrosRenap")
	public RestResponse obtenerRegistrosRenap(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody Object element) throws CustomException {

		authTokenHeader = request.getHeader("Authorization");
		try {
			
			data.setObject(element);
			if (data.getValue("fecha")==null || data.getValue("fecha")=="" ) return new RestResponse(null,new CustomException("Ingrese la fecha",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			serviceQuartz.setRepository(rpCabeceraFolio, rpDetalleFolio, rpDetalleHistorico);
			serviceQuartz.setData(element);
			serviceQuartz.setToken(authTokenHeader);
			serviceQuartz.setUserPrincipal(userPrincipal);
			serviceQuartz.runTarea();
			/*captacionRenap.setFecha(data.getValue("fecha").toString());
			captacionRenap.setEntityManagerFactory(entityManagerFactory);
			captacionRenap.setRespository(rpCabeceraFolio, rpDetalleFolio);
			captacionRenap.setData(element);
			captacionRenap.setToken(authTokenHeader);
			captacionRenap.setUserPrincipal(userPrincipal);
			captacionRenap.ObtenerRegistrosDefuncionesRenap();*/
			
			
			if (serviceQuartz.getResponse().getError()!=null)throw new Exception(serviceQuartz.getResponse().getError().toString());
			else {
				response.setData(serviceQuartz.getResponse().getData());
			}
			
		}catch (Exception exception) {
			CustomException customExcepction= new CustomException(exception.getMessage(),ErrorCode.REST_UPDATE,this.getClass().getSimpleName(), 0);
			response.setError(customExcepction);
	    }
		return response;
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("reporteFallecidos")
	public RestResponse ReportesFallecidos(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody Object element) {
		
		try {
			data.setObject(element);
			if (data.getValue("criterio")==null || data.getValue("criterio")=="" ) return new RestResponse(null,new CustomException("Ingrese el criterio de generacion de Reporte",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("fechaInicio")==null || data.getValue("fechaInicio")=="" ) return new RestResponse(null,new CustomException("Ingrese la fecha inicio",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			if (data.getValue("fechaFinal")==null || data.getValue("fechaFinal")=="" ) return new RestResponse(null,new CustomException("Ingrese la fecha final",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			
			captacionRenap.setEntityManagerFactory(entityManagerFactory);
			captacionRenap.setUserPrincipal(userPrincipal);
			captacionRenap.setToken(authTokenHeader);
			captacionRenap.setData(element);
			captacionRenap.setRespository(rpCabeceraFolio, rpDetalleFolio, rpDetalleHistorico);
			captacionRenap.ReportesFallecidos();
			
			if (captacionRenap.getResponse().getError()!=null)throw new Exception(captacionRenap.getResponse().getError().toString());
			else {
				response.setData(captacionRenap.getResponse().getData());
			}
			
		}catch (Exception exception) {
			CustomException customExcepction= new CustomException(exception.getMessage(),ErrorCode.REST_UPDATE,this.getClass().getSimpleName(), 0);
			response.setError(customExcepction);
	    }
		
		return response;
	}
	
	@PostMapping("reporteVerificados")
	public RestResponse ReporteVerificados(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody Object element) throws CustomException {
		
		authTokenHeader = request.getHeader("Authorization");
		
		try {
			
			data.setObject(element);
			if (data.getValue("fecha")==null || data.getValue("fecha")=="" ) return new RestResponse(null,new CustomException("Ingrese la fecha",ErrorCode.REST_CREATE,this.getClass().getSimpleName(),0));
			
			captacionRenap.setEntityManagerFactory(entityManagerFactory);
			captacionRenap.setUserPrincipal(userPrincipal);
			captacionRenap.setToken(authTokenHeader);
			captacionRenap.setData(element);
			captacionRenap.setRespository(rpCabeceraFolio, rpDetalleFolio, rpDetalleHistorico);
			captacionRenap.ObtenerDataVerificados();
			
			if (captacionRenap.getResponse().getError()!=null)throw new Exception(captacionRenap.getResponse().getError().toString());
			else {
				response.setData(captacionRenap.getResponse().getData());
			}
			
		}catch (Exception exception) {
			CustomException customExcepction= new CustomException(exception.getMessage(),ErrorCode.REST_UPDATE,this.getClass().getSimpleName(), 0);
			response.setError(customExcepction);
	    }
		
		return response;
	}
	
	
}
