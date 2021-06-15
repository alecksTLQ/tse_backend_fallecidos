package com.example.springsocial.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import com.example.springsocial.crud.ModelSetGetTransaction;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.model.NoFolioModel;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.RestResponse;

@SuppressWarnings({"rawtypes","unused","unchecked"})
public class NoFolioTransaction {
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	private EntityTransaction transaction = null;
	private EntityManager entityManager = null;
	
	private ModelSetGetTransaction modelTransaction = new ModelSetGetTransaction();
	private ObjectSetGet data  = new ObjectSetGet();
	private NoFolioModel mdlFolio = new NoFolioModel();
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
	
	public NoFolioModel getData() {
		return this.mdlFolio;
	}
	
	public void selectFolio(Long id) {
		modelTransaction.getValueByID(id);
	}
	
	public void insertFolio(NoFolioModel folio) {
		modelTransaction.saveWithFlush(folio);
	}
	
	public void deleteFolio(NoFolioModel folio) {
		modelTransaction.update(folio);
	}
	
	private void confirmTransactionAndSetResult() {
		transaction.commit();
		response.setData(this.mdlFolio);
	}
	
	public RestResponse GetResponse() {
		return this.response;
	}
	
	public void save(NoFolioModel folio) {
		try {
			
			startTransaction();
			insertFolio(folio);
			confirmTransactionAndSetResult();
			
		}catch(Exception e) {
			transaction.rollback();
			response.setError(e.getMessage());
		}finally {
			if(entityManager.isOpen()) entityManager.close();
		}
	}
	
	public void delete(NoFolioModel folio) {
		try {
			startTransaction();
			deleteFolio(folio);
			confirmTransactionAndSetResult();
		}catch(Exception e) {
			transaction.rollback();
			response.setError(e.getMessage());
		}finally {
			if(entityManager.isOpen()) entityManager.close();
		}
	}
	
	public void select(Long folio) {
		try {
			startTransaction();
			 selectFolio(folio);
			confirmTransactionAndSetResult();
		}catch(Exception e) {
			transaction.rollback();
			response.setError(e.getMessage());
		}finally {
			if(entityManager.isOpen()) entityManager.close();
		}
	}
	
}
