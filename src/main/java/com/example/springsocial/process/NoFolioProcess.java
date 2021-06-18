package com.example.springsocial.process;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springsocial.crud.CRUD;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.generic.CrudController;
import com.example.springsocial.model.NoFolioModel;
import com.example.springsocial.repository.ElementRepository;
import com.example.springsocial.repository.EntitiRepository;
import com.example.springsocial.repository.NoFolioRepository;
import com.example.springsocial.service.NoFolioService;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;
import com.example.springsocial.transaction.NoFolioTransaction;


@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class NoFolioProcess implements CrudController {
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private NoFolioRepository rpNoFolio;
	private ElementRepository elementRepository;
	private EntitiRepository entitiRepository;

	
	//NOMBRE MODELO
	private String moduloName = "NoFolioModel";	
	//DATE TOOLS
	private DateTools dateTools = new DateTools();
	RestResponse response = new RestResponse();
	ObjectSetGet error= new ObjectSetGet();
	ObjectSetGet data = new ObjectSetGet();
	
	private NoFolioTransaction folioTransaction = new NoFolioTransaction();
	private List<NoFolioModel> listFolio = null;
	private NoFolioModel mdlFolio, mdlfolio2 = new NoFolioModel();
	
	private CRUD crud = new CRUD();
	
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		if(entityManagerFactory!=null) {
			this.entityManagerFactory = entityManagerFactory;
		}
	}
	
	
	@PostConstruct
    private void init() {
    	crud.setRepository(this.rpNoFolio);
    	crud.setModelName(this.moduloName);
    	crud.setEntitiRepository(this.entitiRepository);
    	crud.setElementRepository(this.elementRepository);
    	repositories.put(this.moduloName, this.rpNoFolio);
    }
	
	public RestResponse create(String Usuario, Integer Cantidad) {
		NoFolioModel folio = new NoFolioModel();
		Integer valor = null;
		Integer año = null;
		
		try {
			año = dateTools.getYearOfCurrentDate();
			valor = rpNoFolio.findByaño(año);
			
			if(valor!=null) {
				for(int i=1;i<=Cantidad;i++) {
					
					folio.setNrofolio(valor+i);
					folio.setAño(año);
					folio.setNrolinea(1);
					folio.setEstado(1);
					folio.setUsuario(Usuario);
					folio.setFechaasignacion(dateTools.get_CurrentDate());
					
					folioTransaction.setEntityManagerFactory(entityManagerFactory);
					folioTransaction.save(folio);	
				}
				
				if(folioTransaction.GetResponse().getError()!=null) throw new Exception(folioTransaction.GetResponse().getError().toString());
				else {
					response.setData("Insercion Folio "+folioTransaction.GetResponse().getData());
				}
			}else {
				response.setError("");
			}
			
		}catch(Exception e) {
			System.out.print(e.getMessage());
			response.setData(e.getMessage());
		}
		
		return response;
	}

	public RestResponse obtener(String usuario) {
		Integer año = null,  folio = null, linea=null;
		
		try {
			año = dateTools.getYearOfCurrentDate();
			folio = rpNoFolio.SelectNoFolio(usuario, año);
			linea = rpNoFolio.SelectNoLinea(folio, año);
			
			if(folio!=null && linea!=null) {
				response.setData(folio+","+linea);
			}else {
				response.setData("Datos nulos");
			}
			
		}catch(Exception e) {
			System.out.print(e.getMessage());
			response.setData(e.getMessage());
		}
		
		return response;
	}
	
	public RestResponse obtenerRegistrosFolios() {
		Integer año = null;
		try {
			
			año = dateTools.getYearOfCurrentDate();
			listFolio = rpNoFolio.findByAllAño(año);
			
			response.setData(listFolio);
			
		}catch(Exception e) {
			System.out.print(e.getMessage());
			response.setData(e.getMessage());
		}
		
		return response;
	}
	
	public RestResponse EliminarFolio(String usuario, Integer folio) {
		boolean respuesta = false;
		Integer año = null;
		try {
			año = dateTools.getYearOfCurrentDate();
			
			mdlFolio = (NoFolioModel) rpNoFolio.findByFolio(folio);
			
			mdlFolio.setEstadoregistro(0);
			
			folioTransaction.setEntityManagerFactory(entityManagerFactory);
			folioTransaction.delete(mdlFolio);
			
			
			if(folioTransaction.GetResponse().getError()!=null) throw new Exception(folioTransaction.GetResponse().getError().toString());
			else {
				response.setData("delete Folio "+folioTransaction.GetResponse().getData().toString());
			}
			
		}catch(Exception e) {
			response.setData(e.getMessage());
		}
		
		return response;
	}

}
