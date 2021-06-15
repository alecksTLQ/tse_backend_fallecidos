package com.example.springsocial.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import com.example.springsocial.crud.ModelSetGetTransaction;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.model.CabeceraFolioModell;
import com.example.springsocial.model.NoFolioModel;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.RestResponse;

@SuppressWarnings({"rawtypes","unused","unchecked"})
public class CabeceraFolioTransaction {
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	private EntityTransaction transaction = null;
	private EntityManager entityManager = null;
	
	private ModelSetGetTransaction modelTransaction = new ModelSetGetTransaction();
	private ObjectSetGet data  = new ObjectSetGet();
	private CabeceraFolioModell mdlCabeceraFolio = new CabeceraFolioModell();
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
	
	public CabeceraFolioModell getData() {
		return this.mdlCabeceraFolio;
	}
	
	public void insertCabeceraFolio(CabeceraFolioModell folio) {
		modelTransaction.saveWithFlush(folio);
	}
	
	private void confirmTransactionAndSetResult() {
		transaction.commit();
		response.setData(this.mdlCabeceraFolio);
	}
	
	public RestResponse GetResponse() {
		return this.response;
	}
	
	public void save(CabeceraFolioModell folio) {
		try {
			
			startTransaction();
			insertCabeceraFolio(folio);
			confirmTransactionAndSetResult();
			
		}catch(Exception e) {
			transaction.rollback();
			response.setError(e.getMessage());
		}finally {
			if(entityManager.isOpen()) entityManager.close();
		}
	}
	
	
	
}
