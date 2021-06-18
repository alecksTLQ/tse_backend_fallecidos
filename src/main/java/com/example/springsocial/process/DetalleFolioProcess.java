package com.example.springsocial.process;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.springsocial.crud.CRUD;
import com.example.springsocial.generic.CrudController;
import com.example.springsocial.model.DetalleFolio;
import com.example.springsocial.repository.DetalleFolioRepository;
import com.example.springsocial.repository.ElementRepository;
import com.example.springsocial.repository.EntitiRepository;
import com.example.springsocial.transaction.DetalleFolioTransaction;

@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class DetalleFolioProcess implements CrudController{

	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	private DetalleFolioRepository prDetalleFolio;
	private ElementRepository elementRepository;
	private EntitiRepository entitiRepository;
	
	private String moduloName = "DetalleFolio";
	private DetalleFolioTransaction mdlTransaction = new DetalleFolioTransaction();
	private CRUD crud = new CRUD();
	
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		if(entityManagerFactory!=null) {
			this.entityManagerFactory = entityManagerFactory;
		}
	}
	
	@PostConstruct
    private void init() {
    	crud.setRepository(this.prDetalleFolio);
    	crud.setModelName(this.moduloName);
    	crud.setEntitiRepository(this.entitiRepository);
    	crud.setElementRepository(this.elementRepository);
    	repositories.put(this.moduloName, this.prDetalleFolio);
    }
	
	public String createDetalle(DetalleFolio mdlDetalle, EntityManagerFactory entityManagerFactory) {
		String respuesta = null;
		
		try {
			
			mdlTransaction.setEntityManagerFactory(entityManagerFactory);
			mdlTransaction.saveDetalle(mdlDetalle);
			
			if(mdlTransaction.GetResponse().getError()!=null)throw new Exception(mdlTransaction.GetResponse().getError().toString());
				else {
				respuesta = "Insercion correcta";
			}
		}catch(Exception e) {
			respuesta = e.getMessage();
		}
		
		return respuesta;
		
	}
	
	public String updateDetalle(DetalleFolio mdlDetalle, EntityManagerFactory entityManagerFactory) {
		String respuesta = "";
		try {
			
			mdlTransaction.setEntityManagerFactory(entityManagerFactory);
			mdlTransaction.updateDetalle(mdlDetalle);
			
			if(mdlTransaction.GetResponse().getError()!=null)throw new Exception(mdlTransaction.GetResponse().getError().toString());
			else {
				respuesta = "Actualizacion correcta";
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			respuesta = e.getMessage();
		}
		
		return respuesta;
	}
	
}
