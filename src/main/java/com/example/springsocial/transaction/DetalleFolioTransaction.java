package com.example.springsocial.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

import com.example.springsocial.crud.ModelSetGetTransaction;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.model.CabeceraFolioModell;
import com.example.springsocial.model.DetalleFolio;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.RestResponse;

@SuppressWarnings({"rawtypes","unused","unchecked"})
public class DetalleFolioTransaction {
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	private EntityTransaction transaction = null;
	private EntityManager entityManager = null;
	
	private ModelSetGetTransaction modelTransaction = new ModelSetGetTransaction();
	private ObjectSetGet data  = new ObjectSetGet();
	
	private DetalleFolio mdlDetalleFolio = new DetalleFolio();
	private UserPrincipal userPrincipal = null;
	private RestResponse response = new RestResponse();
	
	public void setUserPrincipal(UserPrincipal userPrincipal) {
		this.userPrincipal = userPrincipal;
	}
	
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		if(entityManagerFactory!=null) this.entityManagerFactory = entityManagerFactory;
	}
	
	public void SetData(Object createElement) {
		data.setObject(createElement);
	}
	
	public ObjectSetGet getData2() {
		return data;
	}
	
	private void startTransaction() {
		this.entityManager = this.entityManagerFactory.createEntityManager();
		this.transaction = this.entityManager.getTransaction();
		this.transaction.begin();
		this.modelTransaction.setEntityManager(entityManager);
	}
	
	public DetalleFolio getData() {
		return this.mdlDetalleFolio;
	}
	
	public void insertDetalleFolio(DetalleFolio mdlDetalle) {
		modelTransaction.saveWithFlush(mdlDetalle);
	}
	
	public void updateDetalleFolio(DetalleFolio mdlDetalle) {
		modelTransaction.update(mdlDetalle);
	}
	
	private void confirmTransactionAndSetResult() {
		transaction.commit();
		response.setData(this.mdlDetalleFolio);
	}
	
	public RestResponse GetResponse() {
		return this.response;
	}
	
	public void saveDetalle(DetalleFolio mdlDetalle) {
		try {
			
			startTransaction();
			insertDetalleFolio(mdlDetalle);
			confirmTransactionAndSetResult();
			
		}catch(Exception e) {
			transaction.rollback();
			response.setError(e.getMessage());
		}finally {
			if(entityManager.isOpen()) entityManager.close();
		}
	}
	
	public void updateDetalle(DetalleFolio mdlDetalle) {
		try {
			startTransaction();
			updateDetalleFolio(mdlDetalle);
			confirmTransactionAndSetResult();
			
		}catch(Exception e) {
			transaction.rollback();
			response.setError(e.getMessage());
		}finally {
			if(entityManager.isOpen()) entityManager.close();
		}
	}
	
	
	
}
