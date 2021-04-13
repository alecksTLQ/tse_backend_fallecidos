package com.example.springsocial.crud;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.example.springsocial.customSpecification.PredicateObject;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.generic.StringTools;
import com.example.springsocial.tools.SearchCriteriaTools;

@SuppressWarnings({"unchecked","rawtypes"})
public class ModelSetGetTransaction<T> {
	private Class<T> type;
	private StringTools stringTools = new StringTools();
	private String modelName;
	static final String modelPathFolder = "com.example.springsocial.model";
	private EntityManager entityManager=null; 
	private CriteriaBuilder criteriaBuilder=null;
	private CriteriaQuery<T> criteriaQuery=null;
	private SearchCriteriaTools searchCriteriaTools= new SearchCriteriaTools();
	private Root<T> elementType=null;
	private Object result=null;
	private PredicateObject predicateObject= new PredicateObject();	
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
		setTypeByName();
	}
 	
	public void save(Object object) {
 		this.entityManager.persist(object);
 		this.setResult(object);
 	}
	
	public void update(Object object) {
		this.entityManager.merge(object);
		this.entityManager.flush();
		this.setResult(object);
	}
	
	public void saveWithFlush(Object object) {
 		this.entityManager.persist(object);
 		this.entityManager.flush();
 		this.setResult(object);
 	}
 	
	public void getValueByID(Long id) {
		this.setResult(entityManager.find(this.type, id));
	}
	
	public void getValue() throws CustomException {
		if (this.searchCriteriaTools.getSearchCriteria().isPresent()) {
			predicateObject.setSearchCriteria(searchCriteriaTools.getSearchCriteria());
			predicateObject.setRoot(elementType);
			predicateObject.setCriteriaBuilder(criteriaBuilder);
			
			Predicate predicateList =  predicateObject.get();			
			if (predicateList!=null)
				criteriaQuery.where(criteriaBuilder.and(predicateList));	
		}
		List<T> result = this.entityManager.createQuery(criteriaQuery).getResultList();
		if (result.size()==0) this.setResult(null);
		else this.setResult(result.get(0));
	}
	
	public void getListValue() throws CustomException {
		if (this.searchCriteriaTools.getSearchCriteria().isPresent()) {
			predicateObject.setSearchCriteria(searchCriteriaTools.getSearchCriteria());
			predicateObject.setRoot(elementType);
			predicateObject.setCriteriaBuilder(criteriaBuilder);
			Predicate predicateList =  predicateObject.get();	
			if (predicateList!=null)
				criteriaQuery.where(criteriaBuilder.and(predicateList));	
		}
		this.setResult(this.entityManager.createQuery(criteriaQuery).getResultList());
	}
	 
	private void setTypeByName() {
		try {
			stringTools.setValue(this.modelName);
			stringTools.capitalize();
			this.type= (Class<T>) Class.forName(modelPathFolder+"."+stringTools.getValue());
			this.criteriaQuery = criteriaBuilder.createQuery(this.type);
			this.elementType = criteriaQuery.from(this.type);
		} catch (ClassNotFoundException e) {
			this.type= null;
		}
	}

	public void setType(Object type) {
		this.type= (Class<T>) type;	
		this.criteriaQuery = criteriaBuilder.createQuery(this.type);
		this.elementType = criteriaQuery.from(this.type);
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		criteriaBuilder =  this.entityManager.getCriteriaBuilder();
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public SearchCriteriaTools getSearchCriteriaTools() {
		return searchCriteriaTools;
	}

	public void setSearchCriteriaTools(SearchCriteriaTools searchCriteriaTools) {
		this.searchCriteriaTools = searchCriteriaTools;
	}
}
