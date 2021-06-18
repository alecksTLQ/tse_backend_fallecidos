package com.example.springsocial.controller;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.example.springsocial.model.CabeceraFolioModel;
import com.example.springsocial.model.CabeceraFolioModell;
import com.example.springsocial.model.CaptacionModel;
import com.example.springsocial.model.DetalleFolio;
import com.example.springsocial.model.IdCabecera;
import com.example.springsocial.model.NoFolioModel;
import com.example.springsocial.process.CabeceraFolioProcess;
import com.example.springsocial.process.DetalleFolioProcess;
import com.example.springsocial.repository.CabeceraFolioRepository;
import com.example.springsocial.repository.CabeceraFolioRepository2;
import com.example.springsocial.repository.DetalleFolioRepository;
import com.example.springsocial.repository.ElementRepository;
import com.example.springsocial.repository.EntitiRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;
import com.example.springsocial.transaction.CabeceraFolioTransaction;
import com.example.springsocial.transaction.NoFolioTransaction;


@SuppressWarnings({"rawtypes", "unchecked","unused"})
@RestController
@RequestMapping("EmisionesDpi")
public class CaptacionController <T> implements CrudController{
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	CabeceraFolioRepository rpCabeceraFolio;
	@Autowired
	CabeceraFolioRepository2 rpcabecera;
	@Autowired
	DetalleFolioRepository rpDetalleFolio;
	
	
	//DATE TOOLS
	private DateTools dateTools = new DateTools();
	ObjectSetGet data = new ObjectSetGet();
	RestResponse response = new RestResponse();
	ObjectSetGet error= new ObjectSetGet();
	
	private CabeceraFolioProcess mdlProcess = new CabeceraFolioProcess();
	private DetalleFolioProcess mdlDetalleProces = new DetalleFolioProcess();
	
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("createCaptacion")
	public RestResponse createCaptacion(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody CaptacionModel mdlfolio)
	{
		CabeceraFolioModell mdlFolio = new CabeceraFolioModell();
		DetalleFolio mdlDetalle= new DetalleFolio();
		String respuesta = "";
		String authTokenHeader = request.getHeader("Authorization");
		Integer maxfolio = null, año=null, linea = null;
		try {
			
			año = dateTools.getYearOfCurrentDate();
				//VERIFICAR LA ULTIMA INSERCION EN CABECERAFOLIO PARA EL USUARIO ESTE USUARIO Y AÑO ACTUAL
				maxfolio = rpCabeceraFolio.selectMaxFolio(mdlfolio.getUsuario(), año);
				
				if(maxfolio!=null) {
					linea = rpCabeceraFolio.selectLineaFolio(maxfolio, año);				
				}
				
				if(linea!=null) {
					
					if(linea<10) {
						/* CABECERA FOLIO */
						mdlFolio.setNROFOLIO(maxfolio);
						mdlFolio.setAÑOFOLIO(año);
						mdlFolio.setCODDEPTO(mdlfolio.getCodigdepartemento());
						mdlFolio.setCODMUNIC(mdlfolio.getCodigomunicipio());
						mdlFolio.setFECACTUA(dateTools.get_CurrentDate());
						mdlFolio.setFECSISTE(dateTools.get_CurrentDate());
						mdlFolio.setLINEA(linea+1);
						mdlFolio.setUSRACTUA(mdlfolio.getUsuario());
						mdlFolio.setVERIFICA("v");
						
						/* DETALLE FOLIO */
						mdlDetalle.setNROFOLIO(maxfolio);
						mdlDetalle.setAÑOFOLIO(año);
						mdlDetalle.setNROLINEA(linea+1);
						mdlDetalle.setAPE1FALLE(mdlfolio.getApellidofallecido1());
						mdlDetalle.setAPE2FALLE(mdlfolio.getApellidofallecido2());
						mdlDetalle.setAPE3FALLE(mdlfolio.getApellidofallecido3());
						mdlDetalle.setNOM1FALLE(mdlfolio.getNombrefallecido1());
						mdlDetalle.setNOM2FALLE(mdlfolio.getNombrefallecido2());
						mdlDetalle.setAPE1PADRE(mdlfolio.getApellidopadre1());
						mdlDetalle.setAPE2PADRE(mdlfolio.getApellidopadre2());
						mdlDetalle.setNOMPADRE(mdlfolio.getNombrepadre());
						mdlDetalle.setAPE1MADRE(mdlfolio.getApellidomadre1());
						mdlDetalle.setAPE2MADRE(mdlfolio.getApellidomadre2());
						mdlDetalle.setNOMMADRE(mdlfolio.getNombremadre());
						mdlDetalle.setFECHADEFU(mdlfolio.getFechadefuncion());
						mdlDetalle.setNROORDEN(mdlfolio.getNroorden());
						mdlDetalle.setNROREGIST(mdlfolio.getNroregistro());
						mdlDetalle.setNROBOLETA(mdlfolio.getNroboleta());
						mdlDetalle.setFECHANACI(mdlfolio.getFechanacimiento());
						mdlDetalle.setFECOPERA(mdlfolio.getFechaoperacion());
						mdlDetalle.setUSRDIGITA(mdlfolio.getUsuario());
						mdlDetalle.setESTATUS(mdlfolio.getEstatus());
						mdlDetalle.setCUI(mdlfolio.getCUI());
						
						mdlProcess.setEntityManagerFactory(entityManagerFactory);
						respuesta = respuesta + mdlProcess.updateCabecera(mdlFolio, entityManagerFactory);
						
						respuesta = respuesta + mdlDetalleProces.createDetalle(mdlDetalle, entityManagerFactory);
					}else {
						respuesta = respuesta + modelos(año, mdlfolio);
					}
					
				}else {
					respuesta = respuesta + modelos(año, mdlfolio);				
				}
				
				response.setData(respuesta);
			
			
		}catch(Exception exception) {
			CustomException customExcepction=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(exception); 
			response.setError(customExcepction);
		}
		
		return response;
	}
	
	
	@PreAuthorize("hasRole('USER')")
	@PutMapping("getCaptacion")
	public RestResponse getCabecerayDetalle() {
		
		
		
		return response;
	}
	
	
	@PreAuthorize("hasRole('USER')")
	@PutMapping("updateCaptacion")
	public RestResponse updateDetalle(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestBody DetalleFolio mdlfolio) {

		String authTokenHeader = request.getHeader("Authorization");
		String respuesta = "";
		try {
			
			respuesta = respuesta + mdlDetalleProces.updateDetalle(mdlfolio,entityManagerFactory);
			response.setData(respuesta);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			response.setData(e.getMessage());
		}
		
		
		return response;
	}
	
	
	public Integer consultarCaptacion(CaptacionModel mdl) {
		Integer existe = null;
		List<DetalleFolio> mdlResultado = null;

		mdlResultado = rpDetalleFolio.consultarCaptacion(
				mdl.getNroboleta(), mdl.getCUI(), mdl.getNroorden(), mdl.getNroregistro()
		);
		
		if(mdlResultado.size()>0) {
			existe = 1;
		}else {
			existe = 0;
		}
		
		return existe;
	}

	public String modelos(Integer año, CaptacionModel mdlfolio) {
		CabeceraFolioModell mdlFolio = new CabeceraFolioModell();
		DetalleFolio mdlDetalle = new DetalleFolio();
		String respuesta = "";
		/* CABECERA FOLIO */
		mdlFolio.setAÑOFOLIO(año);
		mdlFolio.setCODDEPTO(mdlfolio.getCodigdepartemento());
		mdlFolio.setCODMUNIC(mdlfolio.getCodigomunicipio());
		mdlFolio.setFECACTUA(dateTools.get_CurrentDate());
		mdlFolio.setFECSISTE(dateTools.get_CurrentDate());
		mdlFolio.setLINEA(1);
		mdlFolio.setUSRACTUA(mdlfolio.getUsuario());
		mdlFolio.setVERIFICA("v");
		
		mdlProcess.setEntityManagerFactory(entityManagerFactory);
		respuesta = respuesta + mdlProcess.createCabecera(mdlFolio, entityManagerFactory);

		Integer nro = rpCabeceraFolio.selectMaxFolio(mdlfolio.getUsuario(), año);
		
		/* DETALLE FOLIO */
		mdlDetalle.setNROFOLIO(nro);
		mdlDetalle.setAÑOFOLIO(año);
		mdlDetalle.setNROLINEA(1);
		mdlDetalle.setAPE1FALLE(mdlfolio.getApellidofallecido1());
		mdlDetalle.setAPE2FALLE(mdlfolio.getApellidofallecido2());
		mdlDetalle.setAPE3FALLE(mdlfolio.getApellidofallecido3());
		mdlDetalle.setNOM1FALLE(mdlfolio.getNombrefallecido1());
		mdlDetalle.setNOM2FALLE(mdlfolio.getNombrefallecido2());
		mdlDetalle.setAPE1PADRE(mdlfolio.getApellidopadre1());
		mdlDetalle.setAPE2PADRE(mdlfolio.getApellidopadre2());
		mdlDetalle.setNOMPADRE(mdlfolio.getNombrepadre());
		mdlDetalle.setAPE1MADRE(mdlfolio.getApellidomadre1());
		mdlDetalle.setAPE2MADRE(mdlfolio.getApellidomadre2());
		mdlDetalle.setNOMMADRE(mdlfolio.getNombremadre());
		mdlDetalle.setFECHADEFU(mdlfolio.getFechadefuncion());
		mdlDetalle.setNROORDEN(mdlfolio.getNroorden());
		mdlDetalle.setNROREGIST(mdlfolio.getNroregistro());
		mdlDetalle.setNROBOLETA(mdlfolio.getNroboleta());
		mdlDetalle.setFECHANACI(mdlfolio.getFechanacimiento());
		mdlDetalle.setFECOPERA(mdlfolio.getFechaoperacion());
		mdlDetalle.setUSRDIGITA(mdlfolio.getUsuario());
		mdlDetalle.setESTATUS(mdlfolio.getEstatus());
		mdlDetalle.setCUI(mdlfolio.getCUI());
		
		respuesta = respuesta + mdlDetalleProces.createDetalle(mdlDetalle, entityManagerFactory);	
		
		return respuesta;
	}
	
	
	/* PRUEBAS */
	@PostMapping("pruebaCabecera")
	public RestResponse Cabecera() {
		Integer añoactual = 2022, maxañoregistrado= null, folio = null;
		CabeceraFolioModel mdl = new CabeceraFolioModel();
		
		maxañoregistrado = rpcabecera.obtenerMaxAño();
		
		if(maxañoregistrado!=null) {
			if(añoactual>maxañoregistrado) {
				folio = rpcabecera.obtenerMaxFolioWhereAño(añoactual);
				folio = folio -1;
				folio = folio * -1;
				
				rpcabecera.alterSequence(folio);
				rpcabecera.alterSequence2();
				rpcabecera.reestablecerSequence();
			}
		}
		
		
		
		mdl.setAÑOFOLIO(añoactual);
		
		rpcabecera.save(mdl);
		
		return response;
	}
	

}