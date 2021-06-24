package com.example.springsocial.process;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springsocial.api.ApiRenapImages;
import com.example.springsocial.crud.ModelSetGetTransaction;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.model.CabeceraFolioModelN;
import com.example.springsocial.model.DetalleFolioModelN;
import com.example.springsocial.model.Idpaquete;
import com.example.springsocial.model.IdpaqueteDetalle;
import com.example.springsocial.repository.DetalleFolioRepositoryN;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;
import com.example.springsocial.tools.SearchCriteriaTools;


@SuppressWarnings({"rawtypes","unchecked","unused"})
public class CaptacionFallecidoProcess {

	private EntityManagerFactory entityManagerFactory;
	private EntityTransaction transaction  = null;
	private EntityManager entityManager = null;
	//REPOSITORY
	@Autowired
	private DetalleFolioRepositoryN rpDetalle;
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
	public void setNroFolio(String Nrofolio) {
		this.nrofolio = Nrofolio;
	}
	
	private void startTransaction() {
		this.entityManager = this.entityManagerFactory.createEntityManager();
		this.transaction = this.entityManager.getTransaction();
		this.transaction.begin();
		this.modelTransaction.setEntityManager(entityManager);
	}
	
	private void busqueda() throws NumberFormatException, Exception, CustomException {
		
		switch(Integer.valueOf(data.getValue("criterio").toString())) {
		case 1:
			//CUI
			modelTransaction.setType(DetalleFolioModelN.class);
			searchCriteriaTools.addIgualAnd("CUI", data.getValue("cui").toString());
			
			modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
			modelTransaction.getValue();
			this.mdlDetalle = (DetalleFolioModelN) modelTransaction.getResult();
			response.setData(mdlDetalle);
		break;
		case 2:
			//NO. FOLIO =  IDPAQUETE
			Integer año =  dateTools.getYearOfCurrentDate();
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
			searchCriteriaTools.addIgualAnd("NROBOLETA", data.getValue("nroboleta").toString());
			
			modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
			modelTransaction.getValue();
			this.mdlDetalle = (DetalleFolioModelN) modelTransaction.getResult();
			response.setData(mdlDetalle);
		break;
		default:
				
		}
	}
	
	private Integer setDetallefolio() throws CustomException, Exception {
		Integer año = dateTools.getYearOfCurrentDate();
		Integer indicador = 0;
		//modelTransaction.setType(CabeceraFolioModelN.class);
		modelTransaction.setType(DetalleFolioModelN.class);
		searchCriteriaTools.clear();
		searchCriteriaTools.addIgualAnd("IDPAQUETE", nrofolio);
		searchCriteriaTools.addIgualAnd("AÑOFOLIO", año.toString());
		modelTransaction.setSearchCriteriaTools(searchCriteriaTools);
		modelTransaction.getListValue();
		this.listaDetalle =  (List<DetalleFolioModelN>) modelTransaction.getResult();
		
		if(listaDetalle.size()<=0) {
			insertCabecerayDetalle(0);
		}else {
			if(listaDetalle.size()>0 && listaDetalle.size()<10) {
				insertCabecerayDetalle(listaDetalle.size());
			}else {
				indicador=1;
			}
		}
		
		return indicador;
	}
	
	public void insertCabecerayDetalle(Integer linea) throws NumberFormatException, Exception {
		this.mdlCabecera = new CabeceraFolioModelN();
		this.mdlId = new Idpaquete();
		
		if(linea==0) {
			mdlId.setIDPAQUETE(data.getValue("idpaquete").toString());
			mdlId.setAÑOFOLIO(dateTools.getYearOfCurrentDate());
			mdlCabecera.setId(mdlId);
			mdlCabecera.setORIGEN(Integer.valueOf(data.getValue("origen").toString()));
			mdlCabecera.setCODDEPTO(Integer.valueOf(data.getValue("coddepto").toString()));
			mdlCabecera.setCODMUNIC(Integer.valueOf(data.getValue("codmunic").toString()));
		    mdlCabecera.setFECSISTE(dateTools.get_CurrentDate());
		    mdlCabecera.setUSRACTUA("usaurio prueba");
	    	modelTransaction.saveWithFlush(mdlCabecera);
		}
	    	
		
		this.mdlDetalle = new DetalleFolioModelN();
		mdlDetalle.setIDPAQUETE(data.getValue("idpaquete").toString());
		mdlDetalle.setAÑOFOLIO(dateTools.getYearOfCurrentDate());
		
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
		mdlDetalle.setFECHADEFU(dateTools.convertStringToDate(data.getValue("fechadefuncion").toString()));
		mdlDetalle.setNROORDEN(data.getValue("nroorden").toString());
		mdlDetalle.setNROREGIST(Integer.valueOf( data.getValue("nroregistro").toString()));
		mdlDetalle.setNROBOLETA(Integer.valueOf(data.getValue("nroboleta").toString()));
		mdlDetalle.setFECHANACI(dateTools.convertStringToDate(data.getValue("fechanacimiento").toString()));
		mdlDetalle.setFECHAOPERA(dateTools.get_CurrentDate());
		mdlDetalle.setUSRDIGITA("usuario prueba");
		mdlDetalle.setESTATUS(data.getValue("estado").toString()); // estado en el que se encontraba en Tpadron o 0
		mdlDetalle.setCUI(Long.valueOf(data.getValue("cui").toString()));
		
		modelTransaction.saveWithFlush(mdlDetalle);
	}
	
	private void updateCabecerayDetalle() throws Exception {
		this.mdlCabecera = new CabeceraFolioModelN();
		this.mdlId = new Idpaquete();
		this.mdlDetalle = new DetalleFolioModelN();
		
		mdlId.setIDPAQUETE(data.getValue("idpaquete").toString());
		mdlId.setAÑOFOLIO(Integer.valueOf(data.getValue("añofolio").toString()));
		mdlCabecera.setId(mdlId);
		mdlCabecera.setORIGEN(Integer.valueOf(data.getValue("origen").toString()));
		mdlCabecera.setCODDEPTO(Integer.valueOf(data.getValue("coddepto").toString()));
		mdlCabecera.setCODMUNIC(Integer.valueOf(data.getValue("codmunic").toString()));
	    mdlCabecera.setFECSISTE(dateTools.convertStringToDate(data.getValue("fechasistema").toString()));
		mdlCabecera.setUSRMOD("usuariomod");
		mdlCabecera.setFECMOD(dateTools.get_CurrentDate());
    	modelTransaction.update(mdlCabecera);
    	
    	mdlDetalle.setID(Integer.valueOf(data.getValue("id").toString()));
    	mdlDetalle.setIDPAQUETE(data.getValue("idpaquete").toString());
		mdlDetalle.setAÑOFOLIO(Integer.valueOf(data.getValue("añofolio").toString()));
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
		mdlDetalle.setFECHADEFU(dateTools.convertStringToDate(data.getValue("fechadefuncion").toString()));
		mdlDetalle.setNROORDEN(data.getValue("nroorden").toString());
		mdlDetalle.setNROREGIST(Integer.valueOf( data.getValue("nroregistro").toString()));
		mdlDetalle.setNROBOLETA(Integer.valueOf(data.getValue("nroboleta").toString()));
		mdlDetalle.setFECHANACI(dateTools.convertStringToDate(data.getValue("fechanacimiento").toString()));
		mdlDetalle.setFECHAOPERA(dateTools.convertStringToDate(data.getValue("fechacreacion").toString()));
		mdlDetalle.setUSRDIGITA(data.getValue("usuariocreacion").toString());
		mdlDetalle.setESTATUS(data.getValue("estado").toString());
		mdlDetalle.setCUI(Long.valueOf(data.getValue("cui").toString()));
		mdlDetalle.setFECMOD(dateTools.get_CurrentDate());
		mdlDetalle.setUSRMOD("usuariomod");
		
		modelTransaction.update(mdlDetalle);
	}
	
	private void updateDetalleFolio() throws NumberFormatException, Exception {
		this.mdlDetalle = new DetalleFolioModelN();
		
		mdlDetalle.setID(Integer.valueOf(data.getValue("id").toString()));
    	mdlDetalle.setIDPAQUETE(data.getValue("idpaquete").toString());
		mdlDetalle.setAÑOFOLIO(Integer.valueOf(data.getValue("añofolio").toString()));
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
		mdlDetalle.setFECHADEFU(dateTools.convertStringToDate(data.getValue("fechadefuncion").toString()));
		mdlDetalle.setNROORDEN(data.getValue("nroorden").toString());
		mdlDetalle.setNROREGIST(Integer.valueOf( data.getValue("nroregistro").toString()));
		mdlDetalle.setNROBOLETA(Integer.valueOf(data.getValue("nroboleta").toString()));
		mdlDetalle.setFECHANACI(dateTools.convertStringToDate(data.getValue("fechanacimiento").toString()));
		mdlDetalle.setFECHAOPERA(dateTools.convertStringToDate(data.getValue("fechacreacion").toString()));
		mdlDetalle.setUSRDIGITA(data.getValue("usuariocreacion").toString());
		
		mdlDetalle.setUSRVERIFI("usuario verifica"); /**/
		
		mdlDetalle.setESTATUS(data.getValue("estado").toString());
		mdlDetalle.setCUI(Long.valueOf(data.getValue("cui").toString()));
		mdlDetalle.setFECMOD(dateTools.convertStringToDate(data.getValue("fechamodificacion").toString()));
		mdlDetalle.setUSRMOD(data.getValue("usuariomodificacion").toString());
		
		modelTransaction.update(mdlDetalle);
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
			consultaTpadron();
			
		}catch(Exception exception) {
			transaction.rollback();
			response.setError(exception.getMessage());
		}finally{
    		if (entityManager.isOpen())	entityManager.close();
    	}
	}
}

/*
 "cui": 2210492900101,
      "fecha_INSCRIPCION_DEFUNCION": "2016-11-10 00:00:00.0",
      "depto_INSCRIPCION": 1,
      "munic_INSCRIPCION": 1,
      "numero_INSCRIPCION_DEFUNCION": 159006,
      "tipo_ENTREGA": 11,
      "estatus": 0,
      "pais_DEFUNCION": 320,
      "depto_DEFUNCION": 1,
      "munic_DEFUNCION": 1,
      "causa_DEFUNCION": "TROMBO EMBOLIA PULMONAR CANCER DE MAMA",
      "tercer_NOMBRE_MADRE": null,
      "apellido_CASADA_MADRE": null,
      "tercer_NOMBRE_PADRE": null,
      "entrega": "2021-06-19T19:35:46.000+0000",
      "primer_NOMBRE": "NIDIA",
      "segundo_NOMBRE": "AZUCENA",
      "tercer_NOMBRE": null,
      "primer_APELLIDO": "RODRIGUEZ",
      "segundo_APELLIDO": "ORDOÑEZ",
      "apellido_CASADA": "ORELLANA",
      "genero": "F",
      "fecha_CARGA": "2021-06-19T19:35:46.000+0000",
      "fecha_DEFUNCION": "2016-11-10 00:00:00.0",
      "fecha_NACIMIENTO": "1972-09-05 00:00:00.0",
      "pais_NACIMIENTO": 320,
      "depto_NACIMIENTO": 1,
      "munic_NACIMIENTO": 1,
      "primer_NOMBRE_PADRE": null,
      "segundo_NOMBRE_PADRE": null,
      "primer_APELLIDO_PADRE": null,
      "segundo_APELLIDO_PADRE": null,
      "primer_NOMBRE_MADRE": null,
      "segundo_NOMBRE_MADRE": null,
      "primer_APELLIDO_MADRE": null,
      "segundo_APELLIDO_MADRE": null,
      "orden_CEDULA": "A01",
      "registro_CEDULA": "864677",
      "depto_EXTENSION": 1,
      "munic_EXTENSION": 1
 * */


