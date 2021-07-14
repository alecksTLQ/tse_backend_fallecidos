package com.example.springsocial.process;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springsocial.api.ApiRenapImages;
import com.example.springsocial.api.ApiTPadron;
import com.example.springsocial.api.ApiUpdateTPadron;
import com.example.springsocial.crud.ModelSetGetTransaction;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.model.CabeceraFolioModelN;
import com.example.springsocial.model.DetalleFolioHistoricoModelN;
import com.example.springsocial.model.DetalleFolioModelN;
import com.example.springsocial.model.Idpaquete;
import com.example.springsocial.model.IdpaqueteDetalle;
import com.example.springsocial.repository.CabeceraFolioRepositoryN;
import com.example.springsocial.repository.DetalleFolioHistoricoRepository;
import com.example.springsocial.repository.DetalleFolioRepositoryN;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;
import com.example.springsocial.tools.SearchCriteriaTools;


@SuppressWarnings({"rawtypes","unchecked","unused"})
public class CaptacionFallecidoProcess {

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	private EntityTransaction transaction  = null;
	private EntityManager entityManager = null;
	//REPOSITORY
	private DetalleFolioRepositoryN rpDetalle = null;
	private CabeceraFolioRepositoryN rpCabeceraFolio = null;
	private DetalleFolioHistoricoRepository rpDetalleHistorico = null;
	//TOOLS
	private SearchCriteriaTools searchCriteriaTools= new SearchCriteriaTools();
	private ModelSetGetTransaction modelTransaction =new ModelSetGetTransaction();
	private DateTools dateTools = new DateTools();
	private ObjectSetGet data= new ObjectSetGet();
	private UserPrincipal userPrincipal =null;
	private RestResponse response= new RestResponse();
	private ObjectSetGet responseApi= new ObjectSetGet();
	//MODELOS
	private DetalleFolioModelN mdlDetalle, mdlDetalleTm;
	private DetalleFolioHistoricoModelN mdlDetalleHistorico = null, mdlHistorico = null;
	private CabeceraFolioModelN mdlCabecera, mdlCebeceraTm;
	private List<CabeceraFolioModelN> listCabecera;
	private List<DetalleFolioModelN> listaDetalle;
	private DetalleFolioModelN modelo = null;
	private Idpaquete mdlId;
	//VARIABLES
	private String nrofolio, token, dpipicture, fingerprint, rubric, fechanacimientoAPI="", fechanacimientoBD="";
	private String primerNombre = "", segundoNombre="", primerApe="",segundoApe="",tercerApe="",mdlPrimerNombre="", mdlSegundoNombre="", mdlPrimerApe="", mdlSegundoApe="", mdlTercerApe="";
	private Integer EstadoFallecido = 0;
	private Boolean Misma_Boleta_Diferente_Nombre = false, EncontroCoincidencia = false;
	//APIS
	private ApiRenapImages images = new ApiRenapImages();
	private ApiTPadron tpadron = new ApiTPadron();
	private ApiUpdateTPadron updatePadron = new ApiUpdateTPadron();
	private JSONObject arrayBoleta, arrayDpi, datosmod;
	private JSONArray arrayCedula = null;
	
	public RestResponse getResponse() {return this.response; }
	public void setData(Object createElement) {data.setObject(createElement);}
	public void setUserPrincipal(UserPrincipal userPrincipal) {this.userPrincipal=userPrincipal;}
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {if(entityManagerFactory!=null) this.entityManagerFactory=entityManagerFactory;}
	public void setToken(String token) {	this.token = token;}
	public void setNroFolio(String Nrofolio) {this.nrofolio = Nrofolio;}
	public void setRepository(
			DetalleFolioRepositoryN rpDetalle, 
			CabeceraFolioRepositoryN rpCabeceraFolio,
			DetalleFolioHistoricoRepository rpDetalleHistorico) 
	{this.rpDetalle = rpDetalle; this.rpCabeceraFolio=rpCabeceraFolio; this.rpDetalleHistorico=rpDetalleHistorico;}
	
	private void clear() {
		arrayBoleta = null; 
		arrayDpi = null;
		arrayCedula = null; 
		mdlDetalle = null; 
		mdlDetalleHistorico = null; 
		mdlHistorico = null;
		mdlCabecera = null; 
		listCabecera = null; 
		listaDetalle = null; 
		modelo = null; 
		EstadoFallecido = 0; 
		Misma_Boleta_Diferente_Nombre = false; 
		EncontroCoincidencia = false;
	}
	
	private void datosmodifica() {
		this.datosmod = new JSONObject();
		this.datosmod.put("status", 0);
		this.datosmod.put("usrmod", userPrincipal.getUsername());
		this.datosmod.put("fecmod", dateTools.get_CurrentDate());
		this.datosmod.put("idpro",27);
		this.datosmod.put("fecpro", dateTools.get_CurrentDate());
		this.datosmod.put("puestomod",119);
	}
	
	private void startTransaction() {
		this.entityManager = this.entityManagerFactory.createEntityManager();
		this.transaction = this.entityManager.getTransaction();
		this.transaction.begin();
		this.modelTransaction.setEntityManager(entityManager);
	}
	
	private void busqueda() throws NumberFormatException, Exception, CustomException {
		
		try {
			
			switch(Integer.valueOf(data.getValue("criterio").toString())) {
			case 1:
				//CUI
				modelTransaction.setType(DetalleFolioModelN.class);
				searchCriteriaTools.clear();
				searchCriteriaTools.addIgualAnd("CUI", data.getValue("cui").toString());
				modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
				modelTransaction.getListValue();
				this.listaDetalle = (List<DetalleFolioModelN>) modelTransaction.getResult();
				response.setData(listaDetalle);
				
			break;
			case 2: 
				//NO. FOLIO =  IDPAQUETE //****
				Integer año =  dateTools.getYearOfCurrentDate();
				this.listaDetalle = rpDetalle.selectByFolioAndOrigen(
						Integer.valueOf(data.getValue("idpaquete").toString()),
						año, 
						Integer.valueOf(data.getValue("origen").toString())
				);
				response.setData(listaDetalle);
			break;
			case 3:
				//NO. BOLETA = NO EMPADRONAMIENTO
				modelTransaction.setType(DetalleFolioModelN.class);
				searchCriteriaTools.clear();
				searchCriteriaTools.addIgualAnd("NROBOLETA", data.getValue("nroboleta").toString());
				modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
				modelTransaction.getListValue();
				this.listaDetalle = (List<DetalleFolioModelN>) modelTransaction.getResult();
				response.setData(listaDetalle);
			break;
			case 4:	
				this.listaDetalle = rpDetalle.selectByFechaCreacion(data.getValue("fecha").toString(), Integer.valueOf(data.getValue("origen").toString()), userPrincipal.getUsername());
				response.setData(listaDetalle);
			break;
			case 5:
				this.listaDetalle = rpDetalle.selectByCedula(data.getValue("orden").toString(), Integer.valueOf(data.getValue("registro").toString()));
				response.setData(listaDetalle);
			break;
			case 6:
				//UN NOMBRE UN APELLIDO
				if(data.getValue("nom1").toString()!="" && data.getValue("nom2").toString()=="" && data.getValue("ape1").toString()!="" && data.getValue("ape2").toString()=="" && data.getValue("ape3").toString()=="") {
					listaDetalle = rpDetalle.selectByNombre2(data.getValue("nom1").toString(), data.getValue("ape1").toString());
				}
				//UN NOMBRE Y DOS APELLIDOS
				if(data.getValue("nom1").toString()!="" && data.getValue("nom2").toString()=="" && data.getValue("ape1").toString()!="" && data.getValue("ape2").toString()!="" && data.getValue("ape3").toString()=="") {
					listaDetalle= rpDetalle.selectByNombre3(data.getValue("nom1").toString(), data.getValue("ape1").toString(), data.getValue("ape2").toString());
				}
				//DOS NOMBRES Y PRIMER APELLIDO
				if(data.getValue("nom1").toString()!="" && data.getValue("nom2").toString()!="" && data.getValue("ape1").toString()!="" && data.getValue("ape2").toString()=="" && data.getValue("ape3").toString()=="") {
					listaDetalle= rpDetalle.selectByNombre6(data.getValue("nom1").toString(), data.getValue("nom2").toString(), data.getValue("ape1").toString());
				}
				//DOS NOMBRES Y DOS APELLIDOS
				if(data.getValue("nom1").toString()!="" && data.getValue("nom2").toString()!="" && data.getValue("ape1").toString()!="" && data.getValue("ape2").toString()!="" && data.getValue("ape3").toString()=="") {
					listaDetalle =rpDetalle.selectByNombre4(data.getValue("nom1").toString(), data.getValue("nom2").toString(), data.getValue("ape1").toString(), data.getValue("ape2").toString());
				}
				//DOS NOMBRES Y TRES APELLIDOS
				if(data.getValue("nom1").toString()!="" && data.getValue("nom2").toString()!="" && data.getValue("ape1").toString()!="" && data.getValue("ape2").toString()!="" && data.getValue("ape3").toString()!="") {
					listaDetalle=rpDetalle.selectByNombre5(data.getValue("nom1").toString(), data.getValue("nom2").toString(), data.getValue("ape1").toString(), data.getValue("ape2").toString(), data.getValue("ape3").toString());
				}
				
				if(listaDetalle!=null && listaDetalle.size()>0) {
					response.setData(listaDetalle);
				}else {
					response.setData(null);
				}
			break;
			default:
					
			}
			
		}catch(Exception e) {
			response.setError(e.getMessage());
		}
	}
	
	//METODO DE CONTROL DE INSERCION
	private Integer setDetallefolio() throws CustomException, Exception {
		Integer año = dateTools.getYearOfCurrentDate();
		Integer indicador = 0;
		
		modelTransaction.setType(CabeceraFolioModelN.class);
		searchCriteriaTools.clear();
		searchCriteriaTools.addIgualAnd("IDPAQUETE", nrofolio); //no. folio
		searchCriteriaTools.addIgualAnd("ORIGEN", "1");
		searchCriteriaTools.addIgualAnd("AÑOFOLIO", año.toString());
		
		modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
		modelTransaction.getValue();
		
		this.mdlCabecera =  (CabeceraFolioModelN) modelTransaction.getResult();
		
		if(mdlCabecera==null) {
			insertCabecerayDetalle(0,0l);
		}else {
			if(mdlCabecera.getLINEAFOLIO()>0 && mdlCabecera.getLINEAFOLIO() <10) {
				insertCabecerayDetalle(mdlCabecera.getLINEAFOLIO(), mdlCabecera.getID());
			}else {
				indicador=1;
			}
		}
		//insertCabecerayDetalle(0);
		return indicador;
		
	}
	
	private Long DefinirId() {
		Long id = null;
		try {
			if(rpCabeceraFolio.selectMaxID()!=null) {
				id = rpCabeceraFolio.selectMaxID()+1;
			}else {
				id = 1l;
			}
		}catch(Exception e) {
			e.getMessage();
		}
		return id;
	}

	//METODO DE INSERCION
	public void insertCabecerayDetalle(Integer linea, Long idmodelo) throws NumberFormatException, Exception {
		Long id = null;
		this.mdlCabecera = new CabeceraFolioModelN();
		
		try {
			id = (idmodelo!=0l)? idmodelo : DefinirId();
			mdlCabecera.setID(id);
			mdlCabecera.setIDPAQUETE(Long.valueOf(data.getValue("nrofolio").toString()));
			mdlCabecera.setAÑOFOLIO(dateTools.getYearOfCurrentDate());
			mdlCabecera.setORIGEN(1);
			mdlCabecera.setCODDEPTO(Integer.valueOf(data.getValue("coddepto").toString()));
			mdlCabecera.setCODMUNIC(Integer.valueOf(data.getValue("codmunic").toString()));
			mdlCabecera.setUSRACTUA(userPrincipal.getUsername());
		    mdlCabecera.setFECCRE(dateTools.get_CurrentDate());
		    
		    if(linea==0) {
			    mdlCabecera.setLINEAFOLIO(1);
		    	modelTransaction.saveWithFlush(mdlCabecera);
		    }else {
			    mdlCabecera.setLINEAFOLIO(linea+1);
		    	modelTransaction.update(mdlCabecera);
		    }
		    	
			this.mdlDetalle = new DetalleFolioModelN();
			mdlDetalle.setIDCABECERA(id);
			if(linea==0) {
				mdlDetalle.setNROLINEA(1);
			}else {
				mdlDetalle.setNROLINEA(linea+1);
			}
			mdlDetalle.setAPE1FALLE(data.getValue("apellido1fallecido").toString());
			mdlDetalle.setAPE2FALLE(data.getValue("apellido2fallecido").toString());
			mdlDetalle.setAPE3FALLE(data.getValue("apellido3fallecido").toString());
			mdlDetalle.setNOM1FALLE(data.getValue("nombre1fallecido").toString());
			mdlDetalle.setNOM2FALLE(data.getValue("nombre2fallecido").toString());
			mdlDetalle.setAPE1PADRE(data.getValue("apellido1padre").toString());
			mdlDetalle.setAPE2PADRE(data.getValue("apellido2padre").toString());
			mdlDetalle.setNOMPADRE(data.getValue("nombrepadre").toString());
			mdlDetalle.setAPE1MADRE(data.getValue("apellido1madre").toString());
			mdlDetalle.setAPE2MADRE(data.getValue("apellido2madre").toString());
			mdlDetalle.setNOMMADRE(data.getValue("nombremadre").toString());
			Date fechaDefuncion =  dateTools.convertAndgetDate(data.getValue("fechadefuncion").toString());
			mdlDetalle.setFECHADEFU(fechaDefuncion);
			mdlDetalle.setNROORDEN(data.getValue("nroorden").toString());
			mdlDetalle.setNROREGIST(Integer.valueOf( data.getValue("nroregistro").toString()));
			mdlDetalle.setNROBOLETA(Integer.valueOf(data.getValue("nroboleta").toString()));
			Date fechaNacimiento = dateTools.convertAndgetDate(data.getValue("fechanacimiento").toString());
			mdlDetalle.setFECHANACI(fechaNacimiento);
			mdlDetalle.setFECCRE(dateTools.get_CurrentDate());
			mdlDetalle.setUSRDIGITA(userPrincipal.getUsername());
			mdlDetalle.setUSRVERIFI("N");
			mdlDetalle.setESTATUS(data.getValue("estado").toString()); // estado en el que se encontraba en Tpadron o 0
			mdlDetalle.setCUI(Long.valueOf(data.getValue("cui").toString()));
			mdlDetalle.setESTADODIFERENCIA(0);
			modelTransaction.saveWithFlush(mdlDetalle);
			
		}catch(Exception e) {
			response.setError(e.getMessage());
		}
	}
	
	private void obtenerDatosCabecera() throws Exception, CustomException {
		modelTransaction.setType(CabeceraFolioModelN.class);
		searchCriteriaTools.clear();
		searchCriteriaTools.addIgualAnd("ID", data.getValue("id").toString()); //no. folio
		modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
		modelTransaction.getValue();
		
		this.mdlCebeceraTm =  (CabeceraFolioModelN) modelTransaction.getResult();
	}
	
	private void obtenerDatosDetalle() throws Exception, CustomException {
		modelTransaction.setType(DetalleFolioModelN.class);
		searchCriteriaTools.clear();
		searchCriteriaTools.addIgualAnd("ID", data.getValue("iddetalle").toString()); //no. folio
		modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
		modelTransaction.getValue();
		
		this.mdlDetalleTm  =  (DetalleFolioModelN) modelTransaction.getResult();
	}
	
	private void updateCabecerayDetalle() throws Exception, CustomException {
		
		try {
			
			obtenerDatosCabecera();
			obtenerDatosDetalle();
			this.mdlCabecera = new CabeceraFolioModelN();
			this.mdlDetalle = new DetalleFolioModelN();
			
			mdlCabecera.setID(Long.valueOf(data.getValue("id").toString()));
			mdlCabecera.setIDPAQUETE(mdlCebeceraTm.getIDPAQUETE());
			mdlCabecera.setAÑOFOLIO(mdlCebeceraTm.getAÑOFOLIO());
			mdlCabecera.setORIGEN(mdlCebeceraTm.getORIGEN());
			mdlCabecera.setCODDEPTO(Integer.valueOf(data.getValue("coddepto").toString()));
			mdlCabecera.setCODMUNIC(Integer.valueOf(data.getValue("codmunic").toString()));
			mdlCabecera.setUSRMOD(userPrincipal.getUsername());//usuario que modifica
			mdlCabecera.setFECMOD(dateTools.get_CurrentDate());//fecha de modificacion
			mdlCabecera.setUSRACTUA(mdlCebeceraTm.getUSRACTUA());
			mdlCabecera.setFECCRE(mdlCebeceraTm.getFECCRE());
			mdlCabecera.setLINEAFOLIO(mdlCebeceraTm.getLINEAFOLIO());
	    	modelTransaction.update(mdlCabecera);
	    	
	    	
	    	mdlDetalle.setID(Long.valueOf(data.getValue("iddetalle").toString()));
	    	mdlDetalle.setIDCABECERA(mdlDetalleTm.getIDCABECERA());
			mdlDetalle.setNROLINEA(mdlDetalleTm.getNROLINEA());
			mdlDetalle.setAPE1FALLE(data.getValue("apellido1fallecido").toString());
			mdlDetalle.setAPE2FALLE(data.getValue("apellido2fallecido").toString());
			mdlDetalle.setAPE3FALLE(data.getValue("apellido3fallecido").toString());
			mdlDetalle.setNOM1FALLE(data.getValue("nombre1fallecido").toString());
			mdlDetalle.setNOM2FALLE(data.getValue("nombre2fallecido").toString());
			mdlDetalle.setAPE1PADRE(data.getValue("apellido1padre").toString());
			mdlDetalle.setAPE2PADRE(data.getValue("apellido2padre").toString());
			mdlDetalle.setNOMPADRE(data.getValue("nombrepadre").toString());
			mdlDetalle.setAPE1MADRE(data.getValue("apellido1madre").toString());
			mdlDetalle.setAPE2MADRE(data.getValue("apellido2madre").toString());
			mdlDetalle.setNOMMADRE(data.getValue("nombremadre").toString());			
			mdlDetalle.setFECHADEFU(dateTools.convertAndgetDate(data.getValue("fechadefuncion").toString()));
			mdlDetalle.setNROORDEN(data.getValue("nroorden").toString());
			mdlDetalle.setNROREGIST(Integer.valueOf( data.getValue("nroregistro").toString()));
			mdlDetalle.setNROBOLETA(Integer.valueOf(data.getValue("nroboleta").toString()));
			mdlDetalle.setFECHANACI(dateTools.convertAndgetDate(data.getValue("fechanacimiento").toString()));
			mdlDetalle.setFECCRE(mdlDetalleTm.getFECCRE());
			mdlDetalle.setUSRVERIFI("N");
			mdlDetalle.setUSRDIGITA(mdlDetalleTm.getUSRDIGITA());
			mdlDetalle.setESTATUS(mdlDetalleTm.getESTATUS());
			mdlDetalle.setCUI(Long.valueOf(data.getValue("cui").toString()));
			mdlDetalle.setFECMOD(dateTools.get_CurrentDate());
			mdlDetalle.setUSRMOD(userPrincipal.getUsername());
			mdlDetalle.setESTADODIFERENCIA(mdlDetalleTm.getESTADODIFERENCIA());
			mdlDetalle.setCOINCIDENCIAS(mdlDetalleTm.getCOINCIDENCIAS());
			
			modelTransaction.update(mdlDetalle);
			
		}catch(Exception e) {
			response.setError(e.getMessage());
		}
	}
	
	//METODO DE VERIFICACION
	private void updateDetalleFolio() throws NumberFormatException, Exception {
		this.mdlDetalle = new DetalleFolioModelN();
		modelo = new DetalleFolioModelN();
		try {
			modelo = obtenerinformacionfallecido(Long.valueOf(data.getValue("iddetalle").toString()));
			mdlDetalle.setID(Long.valueOf(data.getValue("iddetalle").toString()));
			mdlDetalle.setIDCABECERA(modelo.getIDCABECERA());
			mdlDetalle.setNROLINEA(modelo.getNROLINEA());
			mdlDetalle.setAPE1FALLE(modelo.getAPE1FALLE());
			mdlDetalle.setAPE2FALLE(modelo.getAPE2FALLE());
			mdlDetalle.setAPE3FALLE(modelo.getAPE3FALLE());
			mdlDetalle.setNOM1FALLE(modelo.getNOM1FALLE());
			mdlDetalle.setNOM2FALLE(modelo.getNOM2FALLE());
			mdlDetalle.setAPE1PADRE(modelo.getAPE1PADRE());
			mdlDetalle.setAPE2PADRE(modelo.getAPE2PADRE());
			mdlDetalle.setNOMPADRE(modelo.getNOMPADRE());
			mdlDetalle.setAPE1MADRE(modelo.getAPE1MADRE());
			mdlDetalle.setAPE2MADRE(modelo.getAPE2MADRE());
			mdlDetalle.setNOMMADRE(modelo.getNOMMADRE());
			mdlDetalle.setFECHADEFU(modelo.getFECHADEFU());
			mdlDetalle.setNROORDEN(modelo.getNROORDEN());
			mdlDetalle.setNROREGIST(modelo.getNROREGIST());
			mdlDetalle.setNROBOLETA(modelo.getNROBOLETA());
			mdlDetalle.setFECHANACI(modelo.getFECHANACI());
			mdlDetalle.setFECCRE(modelo.getFECCRE());
			mdlDetalle.setUSRDIGITA(modelo.getUSRDIGITA());
			mdlDetalle.setUSRVERIFI(userPrincipal.getUsername()); /**/			
			mdlDetalle.setESTATUS(modelo.getESTATUS());
			mdlDetalle.setCUI(modelo.getCUI());
			mdlDetalle.setFECMOD(modelo.getFECMOD());
			mdlDetalle.setUSRMOD(modelo.getUSRMOD());
			mdlDetalle.setESTADODIFERENCIA(modelo.getESTADODIFERENCIA());
			mdlDetalle.setCOINCIDENCIAS(modelo.getCOINCIDENCIAS());
			
			modelTransaction.update(mdlDetalle);
			
		}catch(Exception | CustomException e) {
			response.setError(e.getMessage());
		}
	}
	
	private DetalleFolioModelN obtenerinformacionfallecido(Long id) throws Exception, CustomException {
		DetalleFolioModelN modelo = new DetalleFolioModelN();
		
		modelTransaction.setType(DetalleFolioModelN.class);
		searchCriteriaTools.clear();
		searchCriteriaTools.addIgualAnd("ID", id.toString());
		modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
		modelTransaction.getValue();
		modelo = (DetalleFolioModelN) modelTransaction.getResult();
		
		return modelo;
	}
	
	private void consultaNroBoleta() {
		try {
			if(Integer.valueOf(data.getValue("nroboleta").toString())>0) {
				this.tpadron.clearParms();
				this.tpadron.setGetPath();
				this.tpadron.addParam("criterio","1");
				this.tpadron.addParam("nroboleta", data.getValue("nroboleta").toString());
				this.tpadron.setAuthorizationHeader(this.token);
				this.tpadron.sendPost();
				if(tpadron.getRestResponse().getData()!=null) {
					arrayBoleta = (JSONObject) tpadron.getRestResponse().getData();
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void consultaDpi() {
		try {	
			if(data.getValue("dpi")!=null && data.getValue("dpi")!="") {
				if(Long.valueOf(data.getValue("dpi").toString())>0l) {
					this.tpadron.clearParms();
					this.tpadron.setGetPath();
					this.tpadron.addParam("criterio", "2");
					this.tpadron.addParam("dpi",data.getValue("dpi").toString());
					this.tpadron.setAuthorizationHeader(this.token);
					this.tpadron.sendPost();
					if(tpadron.getRestResponse().getData()!=null) {
						arrayDpi = (JSONObject) tpadron.getRestResponse().getData();
					}
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void consultaCedula() {
		try {
			
			if(data.getValue("registro")!=null && data.getValue("registro")!="" && data.getValue("orden")!=null && data.getValue("orden")!=""){
				this.tpadron.clearParms();
				this.tpadron.setGetPath();
				this.tpadron.addParam("criterio", "3");
				this.tpadron.addParam("registro", data.getValue("registro").toString());
				this.tpadron.addParam("orden", data.getValue("orden").toString());
				this.tpadron.setAuthorizationHeader(this.token);
				this.tpadron.sendPost();
				if(this.tpadron.getRestResponse().getData()!=null ) {
					arrayCedula = (JSONArray) this.tpadron.getRestResponse().getData();
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateTPadron(JSONObject datos,JSONObject datosmod) {
		try {
			this.updatePadron.clearParms();
			this.updatePadron.setGetPath();
			this.updatePadron.addParam("nroboleta", datos.getString("nroboleta"));
			this.updatePadron.addParam("status", datosmod.getString("status"));
			this.updatePadron.addParam("usrmod", datosmod.getString("usrmod"));
			this.updatePadron.addParam("fecmod", datosmod.getString("fecmod"));
			this.updatePadron.addParam("idpro", datosmod.getString("idpro"));
			this.updatePadron.addParam("fecpro", datosmod.getString("fecpro"));
			this.updatePadron.addParam("puestomod", datosmod.getString("puestomod"));
			this.updatePadron.setAuthorizationHeader(this.token);
			this.updatePadron.sendPost();
			if(this.updatePadron.getRestResponse().getData()!=null) {
				response.setData(this.updatePadron.getRestResponse().getData());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void consultaPadron() throws Exception, CustomException {
		try{
			
			if(arrayBoleta==null) {
				consultaDpi();
				if(arrayDpi==null) {
					consultaCedula();
					if(arrayCedula!=null) {
						//proceso(arrayCedula,2);
						procesoCedula(arrayCedula);
					}
				}else {
					proceso(arrayDpi,2);
				}
			}else {
				proceso(arrayBoleta,1);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void BuscarHistorico() throws CustomException {
		modelTransaction.setType(DetalleFolioHistoricoModelN.class);
		searchCriteriaTools.clear();
		
		searchCriteriaTools.addIgualAnd("ID", modelo.getID().toString());
		
		modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
		modelTransaction.getValue();
		this.mdlHistorico  = (DetalleFolioHistoricoModelN) modelTransaction.getResult();
	}
	
	private void Historico(JSONObject datos) throws CustomException {
		this.mdlDetalleHistorico = new DetalleFolioHistoricoModelN();
		
		BuscarHistorico();
		
		mdlDetalleHistorico.setID(modelo.getID());
		mdlDetalleHistorico.setIDCABECERA(modelo.getIDCABECERA());
		mdlDetalleHistorico.setAÑO(dateTools.getYearOfCurrentDate());
		mdlDetalleHistorico.setNROLINEA(modelo.getNROLINEA());
		mdlDetalleHistorico.setAPE1FALLE(modelo.getAPE1FALLE());
		mdlDetalleHistorico.setAPE2FALLE(modelo.getAPE2FALLE());
		mdlDetalleHistorico.setAPE3FALLE(modelo.getAPE3FALLE());
		mdlDetalleHistorico.setNOM1FALLE(modelo.getNOM1FALLE());
		mdlDetalleHistorico.setNOM2FALLE(modelo.getNOM2FALLE());
		mdlDetalleHistorico.setAPE1PADRE(modelo.getAPE1PADRE());
		mdlDetalleHistorico.setAPE2PADRE(modelo.getAPE2PADRE());
		mdlDetalleHistorico.setNOMPADRE(modelo.getNOMPADRE());
		mdlDetalleHistorico.setAPE1MADRE(modelo.getAPE1MADRE());
		mdlDetalleHistorico.setAPE2MADRE(modelo.getAPE2MADRE());
		mdlDetalleHistorico.setNOMMADRE(modelo.getNOMMADRE());
		mdlDetalleHistorico.setFECHADEFU(modelo.getFECHADEFU());
		mdlDetalleHistorico.setNROORDEN(modelo.getNROORDEN());
		mdlDetalleHistorico.setNROREGIST(modelo.getNROREGIST());
		mdlDetalleHistorico.setNROBOLETA(modelo.getNROBOLETA());
		mdlDetalleHistorico.setFECHANACI(modelo.getFECHANACI());
		mdlDetalleHistorico.setFECCRE(modelo.getFECCRE());
		mdlDetalleHistorico.setUSRCRE(modelo.getUSRDIGITA());
		mdlDetalleHistorico.setUSRVERIFI(modelo.getUSRVERIFI());
		mdlDetalleHistorico.setFECMOD(modelo.getFECMOD());
		mdlDetalleHistorico.setUSRMOD(modelo.getUSRMOD());
		mdlDetalleHistorico.setESTATUS(modelo.getESTATUS());
		mdlDetalleHistorico.setESTATUSF(EstadoFallecido);
		if(datos!=null) {
			mdlDetalleHistorico.setDEPTOINS( datos.getInteger("depEmpadronamiento"));
			mdlDetalleHistorico.setMUNICINS( datos.getInteger("munEmpadronamiento"));
			mdlDetalleHistorico.setDEPTOEXT( datos.getInteger("depNacimiento"));
			mdlDetalleHistorico.setMUNICEXT( datos.getInteger("munNacimiento"));
			mdlDetalleHistorico.setREFERENCIAS(datos.getString("statusPadron")+"-"+datos.getString("idProceso")+"-"+datos.getString("puestomodifica"));
		}

		mdlDetalleHistorico.setCUI(modelo.getCUI());
		
		if(mdlHistorico!=null) {
			modelTransaction.update(mdlDetalleHistorico);
		}else {
			modelTransaction.saveWithFlush(mdlDetalleHistorico);
		}
	}
	
	private void comprobarVariables(JSONObject datos) {
		primerNombre = (datos.get("primerNombre")!=null)? datos.getString("primerNombre"):"null";
		segundoNombre = (datos.get("segundoNombre")!=null)? datos.getString("segundoNombre"):"null";
		primerApe = (datos.get("primerApellido")!=null)? datos.getString("primerApellido"):"null";
		segundoApe = (datos.get("segundoApellido")!=null)? datos.getString("segundoApellido"):"null";
		tercerApe = (datos.get("tercerApellido")!=null)? datos.getString("tercerApellido"):"null";
		
		mdlPrimerNombre= (modelo.getNOM1FALLE()!=null)? modelo.getNOM1FALLE() : "null";
		mdlSegundoNombre= (modelo.getNOM2FALLE()!=null)? modelo.getNOM2FALLE() : "null";
		mdlPrimerApe= (modelo.getAPE1FALLE()!=null)? modelo.getAPE1FALLE() : "null";
		mdlSegundoApe= (modelo.getAPE2FALLE()!=null)? modelo.getAPE2FALLE() : "null";
		
		mdlTercerApe=(modelo.getAPE3FALLE()!=null)? modelo.getAPE3FALLE() : "null";
	}
	
	private void proceso(JSONObject datos, Integer opcion) throws CustomException {
		Long boletae = null, boletac=null;
		
		datosmodifica();
		comprobarVariables(datos);
		switch(opcion) {
		case 1:
			boletae = datos.getLong("nroBoleta");
			boletac = Long.valueOf(modelo.getNROBOLETA().toString());
			if(datos.getInteger("statusPadron")>=0 && datos.getInteger("statusPadron")<=17) {
				
				
				if(boletae.toString().equals(boletac.toString()) && primerNombre.equals(mdlPrimerNombre) &&  segundoNombre.equals(mdlSegundoNombre) && primerApe.equals(mdlPrimerApe) && segundoApe.equals(mdlSegundoApe) && tercerApe.equals(mdlTercerApe) ) {
					//ACTUALIZAR EL STATUS EN TPADRON
					if(datos.getInteger("statusPadron")!=0) {
						updateTPadron(datos,datosmod);
					}
					//DEFINE SI EL FALLECIDO FUE ACTUALIZADO O YA ESTABA ACTUALIZADO EN SU ESTADO EN 0
					EstadoFallecido = 8;
					//ACTUALIZA CABECERA, ELIMINA HISTORICO EN CASO DE EXISTIR Y VUELVE A REALIZAR INSERCION A HISTORICO
					Historico(datos); 
					//DELETE EN TABLA DETALLEFOLION
					deleteDetalleFolio();
					EncontroCoincidencia = true;
				}else {
					Misma_Boleta_Diferente_Nombre = true;
				}
			}
		break;
		case 2:
			fechanacimientoAPI = Id(datos.get("fechaNacimiento").toString());
			fechanacimientoBD = Id(modelo.getFECHANACI().toString());
			if(datos.getInteger("statusPadron")>=0 && datos.getInteger("statusPadron")<=17) {
				
				if(fechanacimientoAPI.equals(fechanacimientoBD) &&  primerNombre.equals(mdlPrimerNombre) &&  segundoNombre.equals(mdlSegundoNombre) && primerApe.equals(mdlPrimerApe) && segundoApe.equals(mdlSegundoApe) && tercerApe.equals(mdlTercerApe)   ) {
					//ACTUALIZAR EL STATUS EN TPADRON
					if(datos.getInteger("statusPadron")!=0) {
						updateTPadron(datos,datosmod);
					}
					//DEFINE SI EL FALLECIDO FUE ACTUALIZADO O YA ESTABA ACTUALIZADO EN SU ESTADO EN 0
					EstadoFallecido = 8;
					//ACTUALIZA CABECERA, ELIMINA HISTORICO EN CASO DE EXISTIR Y VUELVE A REALIZAR INSERCION A HISTORICO
					Historico(datos);
					//DELETE EN TABLA DETALLEFOLION
					deleteDetalleFolio();
					EncontroCoincidencia = true;
				}
			}
		break;
		default:
		}
	}
	
	private void procesoCedula(JSONArray datos) throws CustomException {
		Integer posicion = null; JSONObject json =  null;
		
		fechanacimientoBD = Id(modelo.getFECHANACI().toString());
		for(int i=0;i<datos.size();i++) {
			json = new JSONObject(datos.getJSONObject(i));
			fechanacimientoAPI = Id(json.get("fechaNacimiento").toString());
			
			comprobarVariables(json);
			
			if(fechanacimientoAPI.equals(fechanacimientoBD) && primerNombre.equals(mdlPrimerNombre) &&  segundoNombre.equals(mdlSegundoNombre) && primerApe.equals(mdlPrimerApe) && segundoApe.equals(mdlSegundoApe) && tercerApe.equals(mdlTercerApe)   ) {
				posicion = i;
			}
		}
		
		json.clear();
		if(posicion!=null) {
			json = new JSONObject(datos.getJSONObject(posicion));
			
			if(json.getInteger("statusPadron")!=0) {
				updateTPadron(json,datosmod);
			}
			EstadoFallecido = 8;
			
			Historico(json);
			//DELETE EN TABLA DETALLEFOLION
			deleteDetalleFolio();
			EncontroCoincidencia =true;
		}
	}
	
	private void deleteDetalleFolio() {
		try {
			if(modelo!=null) {
				rpDetalle.deleteById(modelo.getID());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Integer NingunResultadoEncontrado() throws CustomException {
		Integer valor = 0;
		JSONObject json =  null;
		if(EncontroCoincidencia==false) {
			EstadoFallecido = 4; // DEFINE QUE LA PERSONA CAPTADA EN FALLECIDOS NO ESTA EMPADRONADO
			valor = 1;
			if(Misma_Boleta_Diferente_Nombre==false) {
				Historico(json);
				//DELETE EN TABLA DETALLEFOLION
				deleteDetalleFolio();
				
			}
		}
		
		return valor;
	}
	
	private void confirmTransactionAndSetResult() {
		transaction.commit();
	}
	
	private JSONObject buscarHistoricoCorreccion() throws CustomException, Exception {
		JSONObject modeloHistorico = new JSONObject();
		modelTransaction.setType(DetalleFolioHistoricoModelN.class);
		searchCriteriaTools.clear();
		searchCriteriaTools.addIgualAnd("ID", data.getValue("iddetalle").toString());
		modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
		modelTransaction.getValue();
		this.mdlDetalleHistorico =  (DetalleFolioHistoricoModelN) modelTransaction.getResult();
		
		if(mdlDetalleHistorico!=null) {
			modeloHistorico.put("nroboleta", mdlDetalleHistorico.getNROBOLETA());
			String [] partes = mdlDetalleHistorico.getREFERENCIAS().split("-");
			
			this.datosmod = new JSONObject();
			this.datosmod.put("status", partes[0]);
			this.datosmod.put("usrmod", userPrincipal.getUsername());
			this.datosmod.put("fecmod", dateTools.get_CurrentDate());
			this.datosmod.put("idpro", partes[1]);
			this.datosmod.put("fecpro", dateTools.get_CurrentDate());
			this.datosmod.put("puestomod",partes[2]);
		}else {
			modeloHistorico.put("error", "FALLECIDO NO ENCONTRADO");
		}
		
		return modeloHistorico;
	}
	
	private void RecuperarDetalle() {
		if(this.mdlDetalleHistorico!=null) {
			this.mdlDetalle = new DetalleFolioModelN();
			
			//this.mdlDetalle.setID(mdlDetalleHistorico.getID());
			this.mdlDetalle.setIDCABECERA(mdlDetalleHistorico.getIDCABECERA());
			this.mdlDetalle.setNROLINEA(mdlDetalleHistorico.getNROLINEA());
			this.mdlDetalle.setAPE1FALLE(mdlDetalleHistorico.getAPE1FALLE());
			this.mdlDetalle.setAPE2FALLE(mdlDetalleHistorico.getAPE2FALLE());
			this.mdlDetalle.setAPE3FALLE(mdlDetalleHistorico.getAPE3FALLE());
			this.mdlDetalle.setNOM1FALLE(mdlDetalleHistorico.getNOM1FALLE());
			this.mdlDetalle.setNOM2FALLE(mdlDetalleHistorico.getNOM2FALLE());
			this.mdlDetalle.setAPE1PADRE(mdlDetalleHistorico.getAPE1PADRE());
			this.mdlDetalle.setAPE2PADRE(mdlDetalleHistorico.getAPE2PADRE());
			this.mdlDetalle.setNOMPADRE(mdlDetalleHistorico.getNOMPADRE());
			this.mdlDetalle.setAPE1MADRE(mdlDetalleHistorico.getAPE1MADRE());
			this.mdlDetalle.setAPE2MADRE(mdlDetalleHistorico.getAPE2MADRE());
			this.mdlDetalle.setNOMMADRE(mdlDetalleHistorico.getNOMMADRE());
			this.mdlDetalle.setFECHADEFU(mdlDetalleHistorico.getFECHADEFU());
			this.mdlDetalle.setNROORDEN(mdlDetalleHistorico.getNROORDEN());
			this.mdlDetalle.setNROREGIST(mdlDetalleHistorico.getNROREGIST());
			this.mdlDetalle.setNROBOLETA(mdlDetalleHistorico.getNROBOLETA());
			this.mdlDetalle.setFECHANACI(mdlDetalleHistorico.getFECHANACI());
			this.mdlDetalle.setFECCRE(mdlDetalleHistorico.getFECCRE());
			this.mdlDetalle.setUSRDIGITA(mdlDetalleHistorico.getUSRCRE());
			this.mdlDetalle.setUSRVERIFI(mdlDetalleHistorico.getUSRVERIFI());
			this.mdlDetalle.setESTATUS(mdlDetalleHistorico.getESTATUS());
			this.mdlDetalle.setCUI(mdlDetalleHistorico.getCUI());
			this.mdlDetalle.setFECMOD(mdlDetalleHistorico.getFECMOD());
			this.mdlDetalle.setUSRMOD(mdlDetalleHistorico.getUSRMOD());
			this.mdlDetalle.setESTADODIFERENCIA(mdlDetalleHistorico.getESTADODIFERENCIA());
			this.mdlDetalle.setCOINCIDENCIAS(mdlDetalleHistorico.getCOINCIDENCIAS());
			
			this.modelTransaction.saveWithFlush(this.mdlDetalle);
		}
	}
	
	private void DeleteDetalleHistorico() {
		try {
			
			rpDetalleHistorico.deleteById(Long.valueOf(data.getValue("iddetalle").toString()));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/* METODOS DEL CONTROLLER */
	public Integer save() throws CustomException {
		Integer indicador = 0;
		try {
			startTransaction();
			indicador = setDetallefolio();
			confirmTransactionAndSetResult();
			
		}catch(Exception exception) {
			transaction.rollback();
			response.setError(exception.getMessage());
		}finally{
    		if (entityManager.isOpen())	entityManager.close();
    	}
		
		return indicador;
	}
	
	public void select() throws CustomException {
	
		try {
			startTransaction();
			busqueda();
			
		}catch(Exception exception) {
			response.setError(exception.getMessage()); 
		}finally{
    		if (entityManager.isOpen())	entityManager.close();
    	}
		
	}
		
	public void update() throws CustomException {
		try {
			startTransaction();
			updateCabecerayDetalle();
			confirmTransactionAndSetResult();
			
		}catch(Exception exception) {
			transaction.rollback();
			response.setError(exception.getMessage());
		}finally{
    		if (entityManager.isOpen())	entityManager.close();
    	}
	}

	public Integer VerificacionyActualizarPadron() throws CustomException {
		Integer valor = 0;
		try {
			startTransaction();
			updateDetalleFolio();
			consultaNroBoleta();
			confirmTransactionAndSetResult();
			
			startTransaction();
			consultaPadron();
			confirmTransactionAndSetResult();
			
			startTransaction();
			valor = NingunResultadoEncontrado();
			confirmTransactionAndSetResult();
			clear();
		}catch(Exception exception) {
			transaction.rollback();
			response.setError(exception.getMessage());
		}finally{
    		if (entityManager.isOpen())	entityManager.close();
    	}
		
		return valor;
	}
	
	public void ProcesoCorrecccion() throws CustomException{
		JSONObject datos = new JSONObject();
		try {
		
			startTransaction();
			datos = buscarHistoricoCorreccion();
			if(datos.containsKey("error")!=true) {
				updateTPadron(datos,datosmod);
			}
			RecuperarDetalle();
			DeleteDetalleHistorico();
			confirmTransactionAndSetResult();
			
		}catch(Exception exception) {
			transaction.rollback();
			response.setError(exception.getMessage());
		}finally{
    		if (entityManager.isOpen())	entityManager.close();
    	}
		/*
		 * 12016	10/11/83	MANUEL	DE JESUS	CHILEL	GALVES		9	19/09/60	12	21	1	3	0	1	1	169	49	843	12 CALLE MIRAFLORES	17-31	11	1	1	999			JASANTIAGO	16/02/15			37	03/11/14	2	119	21-05	
		 * 
		 * */
	}
	
	private String Id(String idpaquete) {
		String Id = "", fecha = "", hora = "";
		char regla1 = 'T';
		char regla2 = ':';
		char regla3 = '.';
		for(int i=0;i<idpaquete.length();i++) {
			
			char c = idpaquete.charAt(i);
			if(i<10) {
				fecha = fecha + c;
			}
			if(i>10 && i<19) {
				hora = hora + c;
			}
		}
		
		return Id = fecha;
	}

}