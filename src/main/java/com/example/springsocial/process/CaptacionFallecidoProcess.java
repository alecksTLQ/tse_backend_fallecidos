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

import com.example.springsocial.api.ApiRenapImages;
import com.example.springsocial.crud.ModelSetGetTransaction;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.model.CabeceraFolioModelN;
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
	private CabeceraFolioModelN mdlCabecera;
	private List<CabeceraFolioModelN> listCabecera;
	private List<DetalleFolioModelN> listaDetalle;
	private Idpaquete mdlId;
	//VARIABLES
	private String nrofolio, token, dpipicture, fingerprint, rubric;
	//APIS
	private ApiRenapImages images = new ApiRenapImages();
	
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
		DetalleFolioModelN modelo = new DetalleFolioModelN();
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
	
	private void consultaTpadron() throws Exception {
		this.images.clearParms();
		this.images.setAuthorizationHeader(this.token);
		this.images.setGetPath("2689143621004");
		this.images.sendGet();
		
		if(images.getRestResponse().getData()!=null) {
			this.responseApi.setObject(images.getRestResponse().getData());
			this.dpipicture = responseApi.getValue("fotoCiudadano").toString();
			this.fingerprint = responseApi.getValue("huellaCiudadano").toString();
			this.rubric = responseApi.getValue("firmaCiudadano").toString();
			
			System.out.println(dpipicture);
		}
	}
	
	
	private void confirmTransactionAndSetResult() {
		transaction.commit();
	}
	
	
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

	public void VerificacionyActualizarPadron() {
		try {
			startTransaction();
			updateDetalleFolio();
			consultaTpadron(); //MATA A LA PERSONA = ACTUALIZA EN EL PADRON A LA PERSONA EN SU ESTADO A FALLECIDO = 0
			confirmTransactionAndSetResult();
		}catch(Exception exception) {
			transaction.rollback();
			response.setError(exception.getMessage());
		}finally{
    		if (entityManager.isOpen())	entityManager.close();
    	}
	}
}