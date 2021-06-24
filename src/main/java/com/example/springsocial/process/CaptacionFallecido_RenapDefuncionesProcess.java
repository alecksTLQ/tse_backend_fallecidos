package com.example.springsocial.process;

import java.text.ParseException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.springsocial.api.ApiDefunciones;
import com.example.springsocial.crud.ModelSetGetTransaction;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.model.CabeceraFolioModelN;
import com.example.springsocial.model.DetalleFolioModelN;
import com.example.springsocial.model.Idpaquete;
import com.example.springsocial.repository.CabeceraFolioRepositoryN;
import com.example.springsocial.repository.DetalleFolioRepositoryN;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;
import com.example.springsocial.tools.SearchCriteriaTools;
import com.fasterxml.jackson.core.JsonProcessingException;

@SuppressWarnings({"rawtypes","unchecked","unused"})
public class CaptacionFallecido_RenapDefuncionesProcess {
	
	
	@Autowired
	private DetalleFolioRepositoryN rpDetalle;
	@Autowired
	private CabeceraFolioRepositoryN rpCabeceraFolio;
	
	private EntityManagerFactory entityManagerFactory;
	private EntityTransaction transaction  = null;
	private EntityManager entityManager = null;
	
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
	private Idpaquete mdlId;
	//VARIABLES 
	private String token, IDPAQUETE=null;
	JSONArray arrayApi = null;
	
	private ApiDefunciones defunciones = new ApiDefunciones();
	
	public RestResponse getResponse() {return this.response; }
	public void setData(Object createElement) {data.setObject(createElement);}
	public void setUserPrincipal(UserPrincipal userPrincipal) {this.userPrincipal=userPrincipal;}
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {if(entityManagerFactory!=null) this.entityManagerFactory=entityManagerFactory;}
	public void setToken(String token) {	this.token = token;}
	
	private void startTransaction() {
		this.entityManager = this.entityManagerFactory.createEntityManager();
		this.transaction = this.entityManager.getTransaction();
		this.transaction.begin();
		this.modelTransaction.setEntityManager(entityManager);
	}
	
	private void confirmTransactionAndSetResult() {
		transaction.commit();
	}
	
	private void consultaTwsDefunciones() throws Exception {
		try {
			this.defunciones.clearParms();
			this.defunciones.setGetPath();
			this.defunciones.setAuthorizationHeader(this.token);
			this.defunciones.addParam("fecha", data.getValue("fecha").toString());
			this.defunciones.addParam("horainicio", data.getValue("horainicio").toString());
			this.defunciones.addParam("horafinal", data.getValue("horafinal").toString());
			
			this.defunciones.sendPost();
			if(defunciones.getRestResponse().getData()!=null) {
				arrayApi = (JSONArray) defunciones.getRestResponse().getData();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void consultaTPadron() {
		/*
		 * CONSULTAS AL API DEL PADRON POR DPI, CEDULA, NOMBRES
		 * POR CADA REGISTRO OBTENIDO EN CONSULTATWSDEFUNCIONES SEGUN FECHA Y HORAS
		 * SE REALIZA COMPARATIVA ENTRE LOS RESULTADOS Y DEPENDIENTO DE LAS COINCIDENCIAS
		 * LOS REGISTROS SE MARCARAN COMO 0, 1, ETC
		 * QUEDARAN LISTOS PARA SU INSERCION A CABECERA Y DETALLE
		 * 
		 * */
		
	}
	
	private void insertCabeceraYDetalle() throws ParseException {
		
		this.mdlCabecera = new CabeceraFolioModelN();
		this.mdlId = new Idpaquete();
		String primernombre=null, segundonombre=null;

		
		for(int x =0; x<arrayApi.size();x++) {
			JSONObject json = new JSONObject(arrayApi.getJSONObject(x));
			
			System.out.println("ciclo: "+x);
			
			if(x==0) {
				IDPAQUETE = json.get("entrega").toString();
				mdlId.setIDPAQUETE(json.get("entrega").toString());
				mdlId.setAÑOFOLIO(dateTools.getYearOfCurrentDate());
				mdlCabecera.setId(mdlId);
				mdlCabecera.setORIGEN(2);
				mdlCabecera.setFECSISTE(dateTools.get_CurrentDate());
				mdlCabecera.setUSRACTUA(userPrincipal.getUsername());
				modelTransaction.saveWithFlush(mdlCabecera);
			}
			this.mdlDetalle = new DetalleFolioModelN();
			mdlDetalle.setIDPAQUETE(IDPAQUETE);
			mdlDetalle.setAÑOFOLIO(dateTools.getYearOfCurrentDate());
			mdlDetalle.setNROLINEA(x+1);
			mdlDetalle.setAPE1FALLE(  (json.get("primer_APELLIDO")!=null)? json.get("primer_APELLIDO").toString():"null" );
			mdlDetalle.setAPE2FALLE(  (json.get("segundo_APELLIDO")!=null)? json.get("segundo_APELLIDO").toString():"null" );
			mdlDetalle.setAPE3FALLE(  (json.get("apellido_CASADA")!=null)? json.get("apellido_CASADA").toString():"null" );
			mdlDetalle.setNOM1FALLE(  (json.get("primer_NOMBRE")!=null)? json.get("primer_NOMBRE").toString():"null" );
			mdlDetalle.setNOM2FALLE(  (json.get("segundo_NOMBRE")!=null)? json.get("segundo_NOMBRE").toString():"null");
			mdlDetalle.setAPE1PADRE(  (json.get("primer_APELLIDO_PADRE")!=null)? json.get("primer_APELLIDO_PADRE").toString():"null"  );
			mdlDetalle.setAPE2PADRE(  (json.get("segundo_APELLIDO_PADRE")!=null)? json.get("segundo_APELLIDO_PADRE").toString():"null");
			
			primernombre = (json.get("primer_NOMBRE_PADRE")!=null)? json.get("primer_NOMBRE_PADRE").toString():"null";
			segundonombre = (json.get("segundo_NOMBRE_PADRE")!=null)?json.get("segundo_NOMBRE_PADRE").toString():"null";
			mdlDetalle.setNOMPADRE(   primernombre+" "+segundonombre    );
			
			mdlDetalle.setAPE1MADRE(  (json.get("primer_APELLIDO_MADRE")!=null)? json.get("primer_APELLIDO_MADRE").toString():"null");
			mdlDetalle.setAPE2MADRE(  (json.get("segundo_APELLIDO_MADRE")!=null)? json.get("segundo_APELLIDO_MADRE").toString():"null");
			
			primernombre = (json.get("primer_NOMBRE_MADRE")!=null)? json.get("primer_NOMBRE_MADRE").toString():"null";
			segundonombre = (json.get("segundo_NOMBRE_MADRE")!=null)? json.get("segundo_NOMBRE_MADRE").toString():"null";
			mdlDetalle.setNOMMADRE(  primernombre+" "+segundonombre);
			
			mdlDetalle.setFECHADEFU(json.getDate("fecha_DEFUNCION"));
			mdlDetalle.setNROORDEN(  (json.get("orden_CEDULA")!=null)? json.get("orden_CEDULA").toString():"nul");
			
			try {
				mdlDetalle.setNROREGIST( (json.get("registro_CEDULA")!=null)? json.getInteger("registro_CEDULA"):0);
			}catch(Exception e) {
				mdlDetalle.setNROREGIST(0);
			}
			
			mdlDetalle.setNROBOLETA(10010); //OBTENER DE CONSULTA TPADRON
			mdlDetalle.setFECHANACI(json.getDate("fecha_NACIMIENTO"));
			mdlDetalle.setFECHAOPERA(dateTools.get_CurrentDate());
			mdlDetalle.setUSRDIGITA(userPrincipal.getUsername());
			mdlDetalle.setESTATUS("0");// segun como se encotraba en TPADRON o puede ser 0
			mdlDetalle.setCUI(  (json.get("cui")!=null)?  Long.valueOf(json.get("cui").toString()):0l);
			modelTransaction.saveWithFlush(mdlDetalle);
			
		}
		
		clear();
	}
	
	public void ObtenerRegistrosDefuncionesRenap() {
		try {
			startTransaction();
			consultaTwsDefunciones();
			consultaTPadron();
			insertCabeceraYDetalle();
			confirmTransactionAndSetResult();
			
		}catch(Exception exception) {
			transaction.rollback();
			response.setError(exception.getMessage());
		}finally{
    		if (entityManager.isOpen())	entityManager.close();
    	}
	}
	
	private String isNull(JSONObject json) {
		String contenedor = null;
		
		try {
			
			
		}catch(Exception e) {
			
		}
		
		return "";
	}
	
	private void clear() {
		IDPAQUETE = null;
		arrayApi.clear();
	}
}
