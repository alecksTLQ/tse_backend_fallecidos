package com.example.springsocial.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.springsocial.api.ApiDefunciones;
import com.example.springsocial.crud.ModelSetGetTransaction;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
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
@Service
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
	private JSONObject Horas = new JSONObject();
	private JSONObject contenedor = new JSONObject();
	private ArrayList<String> arreglo = new ArrayList<String>();
	
	private ObjectSetGet data= new ObjectSetGet();
	private UserPrincipal userPrincipal =null;
	private String user = null;
	private RestResponse response= new RestResponse();
	private ObjectSetGet responseApi= new ObjectSetGet();
	
	//MODELOS
	private DetalleFolioModelN mdlDetalle;
	private CabeceraFolioModelN mdlCabecera;
	private List<DetalleFolioModelN> listaDetalle;
	private List<CabeceraFolioModelN> listaCabeceraFolio;
	private Idpaquete mdlId;
	//VARIABLES 
	private String token, fecha=null,IDPAQUETE=null;//
	JSONArray arrayApi = null;
	
	private ApiDefunciones defunciones = new ApiDefunciones();
	
	public RestResponse getResponse() {return this.response; }
	public void setData(Object createElement) {data.setObject(createElement);}
	public void setUserPrincipal(UserPrincipal userPrincipal) {this.userPrincipal=userPrincipal;}
	public void setUser(String user) {this.user = user;}
	public void setFecha(String fecha) {this.fecha = fecha;}
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {if(entityManagerFactory!=null) this.entityManagerFactory=entityManagerFactory;}
	public void setToken(String token) {	this.token = token;}
	public void setRespository(CabeceraFolioRepositoryN rpCabeceraFolio, DetalleFolioRepositoryN rpDetalle) {this.rpCabeceraFolio = rpCabeceraFolio; this.rpDetalle = rpDetalle;}
	
	private void startTransaction() {
		this.entityManager = this.entityManagerFactory.createEntityManager();
		this.transaction = this.entityManager.getTransaction();
		this.transaction.begin();
		this.modelTransaction.setEntityManager(entityManager);
	}
	
	private void datosVerificados() throws Exception{
		Integer valor = 0;
		this.listaCabeceraFolio = rpCabeceraFolio.selectFolioAndLineas(data.getValue("fecha").toString());

		for(int i=0;i<listaCabeceraFolio.size();i++) {
			CabeceraFolioModelN temp = new CabeceraFolioModelN();
			List<DetalleFolioModelN> tempDetalle = null;
			temp = listaCabeceraFolio.get(i);
/*			valor= rpDetalle.obtenerNoVerificado(temp.getId().getIDPAQUETE(), temp.getId().getAÑOFOLIO());
			tempDetalle = rpDetalle.findByIDPAQUETEAndAÑOFOLIO(temp.getId().getIDPAQUETE(), temp.getId().getAÑOFOLIO());
			
			contenedor.put("folio", temp.getId().getIDPAQUETE());*/
			contenedor.put("cantidadFallecidos", temp.getLINEAFOLIO());
			contenedor.put("No_Verificados", valor);
			contenedor.put("verificados", temp.getLINEAFOLIO()-valor);
			contenedor.put("verificador", tempDetalle.get(0).getUSRVERIFI());
			
			arreglo.add(contenedor.toString());
			contenedor.clear();
		}
		
		response.setData(arreglo);
		
	}
	
	private void confirmTransactionAndSetResult() {
		transaction.commit();
	}
	
	private void consultaTwsDefunciones() throws Exception {
		String fechaMenor = new SimpleDateFormat("dd/MM/yyyy").format(dateTools.restar());
		Horas = dateTools.getHoraActual();
		
		try {
			this.defunciones.clearParms();
			this.defunciones.setGetPath();
			this.defunciones.addParam("fecha",fecha);
			this.defunciones.addParam("horainicio","19:30:00");
			this.defunciones.addParam("horafinal", "19:40:00");
			this.defunciones.setAuthorizationHeader(this.token);
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
		 * EN CASO DE ESTAR EMPADRONADO SE TOMAN LOS DATOS DEL PADRON PARA LA INSERCION
		 * EN CASO DE NO ESTAR EMPADRONADO SE TOMAN LOS DATOS DEL WEB SERVICE
		 * 
		 * */
		
	}
	
	
	private Long DefinirId(Integer opcion) {
		Long id = null;

		switch(opcion) {
		case 1:
			if(rpCabeceraFolio.selectMaxID()!=null) {
				id = rpCabeceraFolio.selectMaxID()+1;
			}else {
				id = 1l;
			}
		break;
		case 2:
			if(rpCabeceraFolio.selectMaxFolio() !=null) {
				id = rpCabeceraFolio.selectMaxFolio() +1;
			}else {
				id = 1l;
			}
		break;
		}
		
		return id;
	}
	
	private void insertCabeceraYDetalle() throws ParseException {
		
		this.mdlCabecera = new CabeceraFolioModelN();
		String primernombre=null, segundonombre=null;
		JSONObject jsontemp = new JSONObject(arrayApi.getJSONObject(0));
		IDPAQUETE = Id(jsontemp.get("entrega").toString());
		
		Long id = DefinirId(1);
		
		for(int x =0; x<arrayApi.size();x++) {
			JSONObject json = new JSONObject(arrayApi.getJSONObject(x));
			
			System.out.println("ciclo: "+x);
			
			mdlCabecera.setID(id);
			mdlCabecera.setIDPAQUETE(DefinirId(2));
			mdlCabecera.setAÑOFOLIO(dateTools.getYearOfCurrentDate());
			//mdlCabecera.setFECHAENTREGA(jsontemp.getDate("entrega"));
			mdlCabecera.setORIGEN(2);
			mdlCabecera.setFECCRE(dateTools.get_CurrentDate());
			mdlCabecera.setUSRACTUA(this.user);
			
			if(x==0) {
				mdlCabecera.setLINEAFOLIO(1);
				modelTransaction.saveWithFlush(mdlCabecera);
			}else {
				mdlCabecera.setLINEAFOLIO(x+1);
				modelTransaction.update(mdlCabecera);
			}
			
			this.mdlDetalle = new DetalleFolioModelN();
			//mdlDetalle.setIDPAQUETE(IDPAQUETE);
			//mdlDetalle.setAÑOFOLIO(dateTools.getYearOfCurrentDate());
			mdlDetalle.setIDCABECERA(id);
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
			mdlDetalle.setNROBOLETA(10010); //OBTENER DE CONSULTA TPADRON || 
			mdlDetalle.setFECHANACI(json.getDate("fecha_NACIMIENTO"));
			mdlDetalle.setFECCRE(dateTools.get_CurrentDate());
			mdlDetalle.setUSRDIGITA(this.user);
			mdlDetalle.setUSRVERIFI("N");
			mdlDetalle.setESTATUS("0");// segun como se encotraba en TPADRON o puede ser 0 en caso de no estar empadronado
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
	
	public void ObtenerDataVerificados() throws CustomException {
		try {
			startTransaction();
			datosVerificados();
			
		}catch(Exception exception) {
			transaction.rollback();
			response.setError(exception.getMessage());
		}finally{
    		if (entityManager.isOpen())	entityManager.close();
    	}
	}
	
	private void clear() {
		IDPAQUETE = null;
		arrayApi.clear();
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
		
		return Id = fecha+"_"+hora;
	}
	
}








