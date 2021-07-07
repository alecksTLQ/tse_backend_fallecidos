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
	//TOOLS
	private SearchCriteriaTools searchCriteriaTools= new SearchCriteriaTools();
	private ModelSetGetTransaction modelTransaction =new ModelSetGetTransaction();
	private DateTools dateTools = new DateTools();
	private ObjectSetGet data= new ObjectSetGet();
	private UserPrincipal userPrincipal =null;
	private RestResponse response= new RestResponse();
	private ObjectSetGet responseApi= new ObjectSetGet();
	//MODELOS
	private DetalleFolioModelN mdlDetalle;
	private DetalleFolioHistoricoModelN mdlDetalleHistorico = null, mdlHistorico = null;
	private CabeceraFolioModelN mdlCabecera;
	private List<CabeceraFolioModelN> listCabecera;
	private List<DetalleFolioModelN> listaDetalle;
	private DetalleFolioModelN modelo = null;
	private Idpaquete mdlId;
	//VARIABLES
	private String nrofolio, token, dpipicture, fingerprint, rubric, fechanacimientoAPI="", fechanacimientoBD="";
	private Integer EstadoFallecido = 0;
	private Boolean Misma_Boleta_Diferente_Nombre = false, EncontroCoincidencia = false;
	//APIS
	private ApiRenapImages images = new ApiRenapImages();
	private ApiTPadron tpadron = new ApiTPadron();
	private ApiUpdateTPadron updatePadron = new ApiUpdateTPadron();
	private JSONObject arrayBoleta, arrayDpi;
	private JSONArray arrayCedula = null;
	
	public RestResponse getResponse() {return this.response; }
	public void setData(Object createElement) {data.setObject(createElement);}
	public void setUserPrincipal(UserPrincipal userPrincipal) {this.userPrincipal=userPrincipal;}
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {if(entityManagerFactory!=null) this.entityManagerFactory=entityManagerFactory;}
	public void setToken(String token) {	this.token = token;}
	public void setNroFolio(String Nrofolio) {this.nrofolio = Nrofolio;}
	public void setRepository(DetalleFolioRepositoryN rpDetalle, CabeceraFolioRepositoryN rpCabeceraFolio) {this.rpDetalle = rpDetalle; this.rpCabeceraFolio=rpCabeceraFolio;}
	
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
				searchCriteriaTools.clear();
				modelTransaction.setType(DetalleFolioModelN.class);
				searchCriteriaTools.addIgualAnd("IDPAQUETE", data.getValue("idpaquete").toString());
				searchCriteriaTools.addIgualAnd("AÑOFOLIO", año.toString());
				modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
				modelTransaction.getListValue();
				this.listaDetalle = (List<DetalleFolioModelN>) modelTransaction.getResult();
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
				this.listaDetalle = rpDetalle.selectByFechaCreacion(data.getValue("fecha").toString(), Integer.valueOf(data.getValue("origen").toString()));
				response.setData(listaDetalle);
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
			mdlCabecera.setORIGEN(Integer.valueOf(data.getValue("origen").toString()));
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
			modelTransaction.saveWithFlush(mdlDetalle);
			
		}catch(Exception e) {
			response.setError(e.getMessage());
		}
	}
	
	private void updateCabecerayDetalle() throws Exception {
		
		try {
			
			this.mdlCabecera = new CabeceraFolioModelN();
			this.mdlDetalle = new DetalleFolioModelN();
			
			mdlCabecera.setID(Long.valueOf(data.getValue("id").toString()));
			mdlCabecera.setIDPAQUETE(Long.valueOf(data.getValue("nrofolio").toString()));
			mdlCabecera.setAÑOFOLIO(Integer.valueOf(data.getValue("año").toString()));
			mdlCabecera.setORIGEN(Integer.valueOf(data.getValue("origen").toString()));
			mdlCabecera.setCODDEPTO(Integer.valueOf(data.getValue("coddepto").toString()));
			mdlCabecera.setCODMUNIC(Integer.valueOf(data.getValue("codmunic").toString()));
			mdlCabecera.setUSRMOD(userPrincipal.getUsername());//usuario que modifica
			mdlCabecera.setFECMOD(dateTools.get_CurrentDate());//fecha de modificacion
			mdlCabecera.setUSRACTUA(data.getValue("usuariocreacion").toString());
			mdlCabecera.setFECCRE(dateTools.convertAndgetDate(data.getValue("fechacreacion").toString()));
			mdlCabecera.setLINEAFOLIO(Integer.valueOf(data.getValue("linea").toString()));
	    	modelTransaction.update(mdlCabecera);
	    	
	    	
	    	mdlDetalle.setID(Long.valueOf(data.getValue("iddetalle").toString()));
	    	mdlDetalle.setIDCABECERA(Long.valueOf(data.getValue("id").toString()));
			mdlDetalle.setNROLINEA(Integer.valueOf(data.getValue("nrolinea").toString()));
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
			mdlDetalle.setFECCRE(dateTools.convertAndgetDate(data.getValue("fechacreacion").toString()));
			mdlDetalle.setUSRVERIFI("N");
			mdlDetalle.setUSRDIGITA(data.getValue("usuariocreacion").toString());
			mdlDetalle.setESTATUS(data.getValue("estado").toString());
			mdlDetalle.setCUI(Long.valueOf(data.getValue("cui").toString()));
			mdlDetalle.setFECMOD(dateTools.get_CurrentDate());
			mdlDetalle.setUSRMOD(userPrincipal.getUsername());
			
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
	
	private void updateTPadron(JSONObject datos) {
		try {
			this.updatePadron.clearParms();
			this.updatePadron.setGetPath();
			this.updatePadron.addParam("nroboleta", datos.get("nroBoleta").toString());
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
					if(arrayCedula==null) {
						
					}else {
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
		}

		mdlDetalleHistorico.setCUI(modelo.getCUI());
		
		if(mdlHistorico!=null) {
			modelTransaction.update(mdlDetalleHistorico);
		}else {
			modelTransaction.saveWithFlush(mdlDetalleHistorico);
		}
	}
	
	private void proceso(JSONObject datos, Integer opcion) throws CustomException {
		Long boletae = null, boletac=null;
		
		
		switch(opcion) {
		case 1:
			boletae = datos.getLong("nroBoleta");
			boletac = Long.valueOf(modelo.getNROBOLETA().toString());
			if(datos.getInteger("statusPadron")>=0 && datos.getInteger("statusPadron")<=17) {
				
				if(boletae.toString().equals(boletac.toString()) && datos.getString("primerNombre").equals(modelo.getNOM1FALLE()) &&  datos.getString("segundoNombre").equals(modelo.getNOM2FALLE()) && datos.getString("primerApellido").equals(modelo.getAPE1FALLE()) && datos.getString("segundoApellido").equals(modelo.getAPE2FALLE()) && datos.getString("tercerApellido").equals(modelo.getAPE3FALLE())   ) {
					//ACTUALIZAR EL STATUS EN TPADRON
					if(datos.getInteger("statusPadron")!=0) {
						updateTPadron(datos);
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
				
				if(fechanacimientoAPI.equals(fechanacimientoBD) &&  datos.getString("primerNombre").equals(modelo.getNOM1FALLE()) &&  datos.getString("segundoNombre").equals(modelo.getNOM2FALLE()) && datos.getString("primerApellido").equals(modelo.getAPE1FALLE()) && datos.getString("segundoApellido").equals(modelo.getAPE2FALLE()) && datos.getString("tercerApellido").equals(modelo.getAPE3FALLE())   ) {
					//ACTUALIZAR EL STATUS EN TPADRON
					if(datos.getInteger("statusPadron")!=0) {
						updateTPadron(datos);
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
			
			if(fechanacimientoAPI.equals(fechanacimientoBD) &&  json.getString("primerNombre").equals(modelo.getNOM1FALLE()) &&  json.getString("segundoNombre").equals(modelo.getNOM2FALLE()) && json.getString("primerApellido").equals(modelo.getAPE1FALLE()) && json.getString("segundoApellido").equals(modelo.getAPE2FALLE()) && json.getString("tercerApellido").equals(modelo.getAPE3FALLE())) {
				posicion = i;
			}
		}
		
		json.clear();
		if(posicion!=null) {
			json = new JSONObject(datos.getJSONObject(posicion));
			
			if(json.getInteger("statusPadron")!=0) {
				updateTPadron(json);
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
	
	
	public void update() {
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
		}catch(Exception exception) {
			transaction.rollback();
			response.setError(exception.getMessage());
		}finally{
    		if (entityManager.isOpen())	entityManager.close();
    	}
		
		return valor;
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