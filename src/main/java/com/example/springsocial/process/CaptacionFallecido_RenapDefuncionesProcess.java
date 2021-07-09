package com.example.springsocial.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.example.springsocial.api.ApiTPadron;
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
	private JSONObject arrayDpi = null;
	private JSONArray arrayCedula = null, arrayHomonimos=null;
	//MODELOS
	private DetalleFolioModelN mdlDetalle;
	private CabeceraFolioModelN mdlCabecera;
	private List<DetalleFolioModelN> listaDetalle;
	private List<CabeceraFolioModelN> listaCabeceraFolio;
	private Idpaquete mdlId;
	//VARIABLES 
	private String token, fecha=null,IDPAQUETE=null;//
	private String primerNombrews = "", segundoNombrews = "", primerApews="", segundoApews="", tercerApews="", cuiws="";
	private String primerNombre = "", segundoNombre="", primerApe="", segundoApe="", tercerApe="", cui="";
	private JSONArray arrayApi = null;
	private Long folio=null, Id=null;
	Integer Linea=0;
	private ApiDefunciones defunciones = new ApiDefunciones();
	
	//APIS
	private ApiTPadron tpadron = new ApiTPadron();
	
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

	private void InsertCabecerayDetalle(JSONObject datos, Integer depto, Integer muni) {
		this.mdlCabecera = new CabeceraFolioModelN();
		this.mdlDetalle = new DetalleFolioModelN();
		String primerNom = "", segundoNom="";
		
		try {
			mdlCabecera.setID(Id);
		    mdlCabecera.setIDPAQUETE(folio);
		    mdlCabecera.setAÑOFOLIO(dateTools.getYearOfCurrentDate());
		    mdlCabecera.setCODDEPTO(depto);
		    mdlCabecera.setCODMUNIC(muni);
		    mdlCabecera.setORIGEN(2);
		    mdlCabecera.setFECCRE(dateTools.get_CurrentDate());
			mdlCabecera.setUSRACTUA(this.user);
			if(Linea==1) {
				mdlCabecera.setLINEAFOLIO(1);
				modelTransaction.saveWithFlush(mdlCabecera);
			}else {
				mdlCabecera.setLINEAFOLIO(Linea);
				modelTransaction.update(mdlCabecera);
			}
			
			mdlDetalle.setIDCABECERA(Id);
			mdlDetalle.setNROLINEA(Linea);
			mdlDetalle.setAPE1FALLE(  (datos.get("primer_APELLIDO")!=null)? datos.get("primer_APELLIDO").toString():"null" );
			mdlDetalle.setAPE2FALLE(  (datos.get("segundo_APELLIDO")!=null)? datos.get("segundo_APELLIDO").toString():"null" );
			mdlDetalle.setAPE3FALLE(  (datos.get("apellido_CASADA")!=null)? datos.get("apellido_CASADA").toString():"null" );
			mdlDetalle.setNOM1FALLE(  (datos.get("primer_NOMBRE")!=null)? datos.get("primer_NOMBRE").toString():"null" );
			mdlDetalle.setNOM2FALLE(  (datos.get("segundo_NOMBRE")!=null)? datos.get("segundo_NOMBRE").toString():"null");
			mdlDetalle.setAPE1PADRE(  (datos.get("primer_APELLIDO_PADRE")!=null)? datos.get("primer_APELLIDO_PADRE").toString():"null"  );
			mdlDetalle.setAPE2PADRE(  (datos.get("segundo_APELLIDO_PADRE")!=null)? datos.get("segundo_APELLIDO_PADRE").toString():"null");
			
			primerNom = (datos.get("primer_NOMBRE_PADRE")!=null)? datos.get("primer_NOMBRE_PADRE").toString():"null";
			segundoNom = (datos.get("segundo_NOMBRE_PADRE")!=null)?datos.get("segundo_NOMBRE_PADRE").toString():"null";
			mdlDetalle.setNOMPADRE(   primerNom+" "+segundoNom    );			
			mdlDetalle.setAPE1MADRE(  (datos.get("primer_APELLIDO_MADRE")!=null)? datos.get("primer_APELLIDO_MADRE").toString():"null");
			mdlDetalle.setAPE2MADRE(  (datos.get("segundo_APELLIDO_MADRE")!=null)? datos.get("segundo_APELLIDO_MADRE").toString():"null");
			primerNom = (datos.get("primer_NOMBRE_MADRE")!=null)? datos.get("primer_NOMBRE_MADRE").toString():"null";
			segundoNom = (datos.get("segundo_NOMBRE_MADRE")!=null)? datos.get("segundo_NOMBRE_MADRE").toString():"null";
			mdlDetalle.setNOMMADRE(  primerNom+" "+segundoNom);
			mdlDetalle.setFECHADEFU(datos.getDate("fecha_DEFUNCION"));
			mdlDetalle.setNROORDEN(  (datos.get("orden_CEDULA")!=null)? datos.get("orden_CEDULA").toString():"nul");
			
			try {
				mdlDetalle.setNROREGIST( (datos.get("registro_CEDULA")!=null)? datos.getInteger("registro_CEDULA"):0);
			}catch(Exception e) {
				mdlDetalle.setNROREGIST(0);
			}
			if(datos.getString("estadoDiferencia").equals("111111")) {
				mdlDetalle.setNROBOLETA(datos.getInteger("boleta")); //OBTENER DE CONSULTA TPADRON ||
				mdlDetalle.setESTATUS(datos.getString("statusPadron"));// segun como se encotraba en TPADRON o puede ser 0 en caso de no estar empadronado
				mdlDetalle.setCOINCIDENCIAS(datos.getString("null"));
			}else {
				mdlDetalle.setNROBOLETA(0);
				mdlDetalle.setESTATUS("0");
				mdlDetalle.setCOINCIDENCIAS(datos.getString("boleta"));
			}
			
			mdlDetalle.setFECHANACI(datos.getDate("fecha_NACIMIENTO"));
			mdlDetalle.setFECCRE(dateTools.get_CurrentDate());
			mdlDetalle.setUSRDIGITA(this.user);
			mdlDetalle.setUSRVERIFI("N");
			mdlDetalle.setCUI(  (datos.get("cui")!=null)?  Long.valueOf(datos.get("cui").toString()):0l);
			mdlDetalle.setESTADODIFERENCIA(datos.getInteger("estadoDiferencia"));
			
			modelTransaction.saveWithFlush(mdlDetalle);
			
			
		}catch(Exception e) {
			response.setError(e.getMessage());
		}
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
			this.defunciones.addParam("fecha","19/06/2021");
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
	
	private void consultaTPadron(JSONArray datos) throws CustomException {
		/*JSONArray datos : es el arreglo de datos que se recibe de la tabla defunciones / datos que provienen de RENAP*/
		Integer deptoDefuncion = 0, municDefuncion = 0, control=0;
		JSONArray contenedor = new JSONArray();
		List<Integer> listaeliminar = new ArrayList<Integer>();
		
		Id = DefinirId(1);
		folio = DefinirId(2);
		
		for(int x =0; x<datos.size();x++) {
			JSONObject json = new JSONObject(datos.getJSONObject(x));
			

				if(control==0) {
					deptoDefuncion = (json.getInteger("depto_DEFUNCION")!=null)? json.getInteger("depto_DEFUNCION"):0;
					municDefuncion = (json.getInteger("munic_DEFUNCION")!=null)? json.getInteger("munic_DEFUNCION"):0;
					control = 1;
				}
				if(json.getInteger("depto_DEFUNCION")==deptoDefuncion && json.getInteger("munic_DEFUNCION")==municDefuncion) {
					System.out.println("depto:"+deptoDefuncion+"  munic:"+municDefuncion+" "+json.getString("primer_NOMBRE"));
					Linea = Linea + 1;
					comparativaProceso(json, deptoDefuncion,municDefuncion);
				}else {
					contenedor.add(json);
				}
		}
		
		if(contenedor.size()>0) {
			consultaTPadron(contenedor);
		}
	}
	
	private void consultaDpi(JSONObject json) {
		try {	
			if(json.containsKey("cui")!=false) {
				
					this.tpadron.clearParms();
					this.tpadron.setGetPath();
					this.tpadron.addParam("criterio", "2");
					this.tpadron.addParam("dpi",json.getString("cui"));
					this.tpadron.setAuthorizationHeader(this.token);
					this.tpadron.sendPost();
					if(tpadron.getRestResponse().getData()!=null) {
						arrayDpi = (JSONObject) tpadron.getRestResponse().getData();
					}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void consultaCedula(JSONObject json) {
		try {
			
			if(json.containsKey("orden_CEDULA")!=false && json.containsKey("registro_CEDULA")!=false){
				this.tpadron.clearParms();
				this.tpadron.setGetPath();
				this.tpadron.addParam("criterio", "3");
				this.tpadron.addParam("registro", json.getString("registro_CEDULA"));
				this.tpadron.addParam("orden", json.getString("orden_CEDULA"));
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
	
	private void consultaNombreHomonimo(JSONObject json) {
		try {
			
			this.tpadron.clearParms();
			this.tpadron.setGetPath();
			this.tpadron.addParam("criterio", "4");
			this.tpadron.addParam("nom1", json.getString("primer_NOMBRE"));
			this.tpadron.addParam("nom2", json.getString("segundo_NOMBRE"));
			this.tpadron.addParam("ape1", json.getString("primer_APELLIDO"));
			this.tpadron.addParam("ape2", json.getString("segundo_APELLIDO"));
			this.tpadron.addParam("ape3", json.getString("apellido_CASADA"));
			this.tpadron.setAuthorizationHeader(this.token);
			this.tpadron.sendPost();
			if(tpadron.getRestResponse().getData()!=null) {
				arrayHomonimos = (JSONArray) tpadron.getRestResponse().getData();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void comparativaProceso(JSONObject json, Integer depto, Integer muni) throws CustomException {
		//ESTE JSON VIENE DEL API DEFUNCIONES POR FECHA Y HORA
		consultaDpi(json);
		if(arrayDpi==null) {
			consultaCedula(json);
			if(arrayCedula==null) {
				consultaNombreHomonimo(json);
				if(arrayHomonimos!=null) {
					
				}
			}else {
				procesoCedula(arrayApi);
			}
		}else {
			proceso(json, depto, muni);
		}
	}
	
	private void comprobarVariables(JSONObject datos){
		primerNombre = (arrayDpi.get("primerNombre")!=null)? arrayDpi.getString("primerNombre"):"null";
		segundoNombre = ( arrayDpi.get("segundoNombre")!=null )? arrayDpi.getString("segundoNombre"):"null";
		primerApe = (arrayDpi.get("primerApellido")!=null)? arrayDpi.getString("primerApellido"):"null";
		segundoApe = ( arrayDpi.get("segundoApellido")!=null )? arrayDpi.getString("segundoApellido"):"null";
		tercerApe = (arrayDpi.get("tercerApellido")!=null)? arrayDpi.getString("tercerApellido"):"null";
		cui = (arrayDpi.getJSONObject("dpi").getString("cui") !=null)? arrayDpi.getJSONObject("dpi").getString("cui"):"null";
		
		primerNombrews = (datos.get("primer_NOMBRE")!=null)? datos.getString("primer_NOMBRE"):"null";
		segundoNombrews = (datos.get("segundo_NOMBRE")!=null)? datos.getString("segundo_NOMBRE"):"null";
		primerApews = (datos.get("primer_APELLIDO")!=null)? datos.getString("primer_APELLIDO"):"null";
		segundoApews = (datos.get("segundo_APELLIDO")!=null)? datos.getString("segundo_APELLIDO"):"null";
		tercerApews = (datos.get("apellido_CASADA")!=null)? datos.getString("apellido_CASADA"):"null";
		cuiws = (datos.get("cui")!=null)? datos.getString("cui"):"null";
	}
	
	private void proceso(JSONObject json, Integer depto, Integer muni) {
		String fechaNacimientoWs = "", fechaNacimientoTpadron = "", estadoDiferencia="";
		comprobarVariables(json);
		
		fechaNacimientoWs = Id(json.get("fecha_NACIMIENTO").toString());
		fechaNacimientoTpadron = Id(arrayDpi.get("fechaNacimiento").toString());
		
		
		if(cui.equals(cuiws)) {
			//INSERCION EN TCABECERA Y TDETALLE
			if(fechaNacimientoTpadron.equals(fechaNacimientoWs)) {
				estadoDiferencia=estadoDiferencia+"1";
			}else {
				estadoDiferencia=estadoDiferencia+"0";
			}
			if(primerNombre.equals(primerNombrews)) {
				estadoDiferencia=estadoDiferencia+"1";
			}else {
				estadoDiferencia=estadoDiferencia+"0";
			}
			if(segundoNombre.equals(segundoNombrews)) {
				estadoDiferencia=estadoDiferencia+"1";
			}else {
				estadoDiferencia=estadoDiferencia+"0";
			}
			if(primerApe.equals(primerApews)) {
				estadoDiferencia=estadoDiferencia+"1";
			}else {
				estadoDiferencia=estadoDiferencia+"0";
			}
			if(segundoApe.equals(segundoApews)) {
				estadoDiferencia=estadoDiferencia+"1";
			}else {
				estadoDiferencia=estadoDiferencia+"0";
			}
			if(tercerApe.equals(tercerApews)) {
				estadoDiferencia=estadoDiferencia+"1";
			}else {
				estadoDiferencia=estadoDiferencia+"0";
			}
			
			
			json.put("estadoDiferencia", estadoDiferencia);
			json.put("boleta", arrayDpi.get("nroBoleta").toString());
			json.put("statusPadron",arrayDpi.get("statusPadron").toString());
			InsertCabecerayDetalle(json, depto,muni);
			arrayDpi.clear();
			arrayDpi = null;
		}
		
	}
	
	@SuppressWarnings("null")
	private void procesoCedula(JSONArray datos) throws CustomException {
		Integer posicion = null; JSONObject json =  null;
		String fechaNacimientoWs = "", fechaNacimientoTpadron = "";
		comprobarVariables(json);
		
		fechaNacimientoWs = Id(json.get("fecha_NACIMIENTO").toString());	
		
		for(int i=0;i<datos.size();i++) {
			json = new JSONObject(datos.getJSONObject(i));
			fechaNacimientoTpadron = Id(json.get("fechaNacimiento").toString());
			
			comprobarVariables(json);
			
			if(fechaNacimientoTpadron.equals(fechaNacimientoWs) && primerNombre.equals(primerNombrews) &&  segundoNombre.equals(segundoNombrews) && primerApe.equals(primerApews) && segundoApe.equals(segundoApews) && tercerApe.equals(tercerApews)   ) {
				posicion = i;
			}
		}
		
		json.clear();
		if(posicion!=null) {
			json = new JSONObject(datos.getJSONObject(posicion));
			//INSERCION
			System.out.println("INSERCION");
		}
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
	
	public void ObtenerRegistrosDefuncionesRenap() throws CustomException {
		try {
			startTransaction();
			consultaTwsDefunciones();
			consultaTPadron(arrayApi);
			//insertCabeceraYDetalle();
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
		arrayDpi.clear();
		arrayCedula.clear();
		arrayHomonimos.clear();
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








