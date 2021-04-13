package com.example.springsocial.crud;

import java.util.List;

import javax.persistence.EntityManager;

import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.model.Element;
import com.example.springsocial.model.Entiti;
import com.example.springsocial.tools.SearchCriteriaTools;

@SuppressWarnings({"rawtypes","unchecked"})
public class ModelValidationTransaction<T> {
	private Class<T> type;
	private EntityManager entityManager=null;
	private ModelSetGetTransaction modelSetGetTransaction  = new ModelSetGetTransaction();
	private SearchCriteriaTools searchCriteriaTools = new SearchCriteriaTools();
	private ElementValidationTransaction elementValidation= new ElementValidationTransaction();
	private ErrorList errorList = new ErrorList();
	private ModelSetGet modelSetGet= new ModelSetGet();
	private Long entitiId;
	private Long companyId;
	private Object model;
	private String modelName;
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) throws CustomException {
		this.modelName = modelName;
		this.setEntitiId();
	}
	
	private void setEntitiId() throws CustomException {
		modelSetGetTransaction.setType(Entiti.class);
		searchCriteriaTools.clear();
		searchCriteriaTools.addIgualAnd("name", this.modelName);
		modelSetGetTransaction.setSearchCriteriaTools(searchCriteriaTools);
		modelSetGetTransaction.getValue();
		Entiti entiti  = (Entiti)modelSetGetTransaction.getResult();
		this.entitiId=entiti.getId();
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
		elementValidation.setCompanyId(this.companyId);
	}

	public void setType(Object type) {
		this.type=(Class<T>) type;
		this.elementValidation.setType(this.type);
	}
	
	public Object getType() {
		return this.type;
	}
	
	public void deleteValidation() throws CustomException {
		searchCriteriaTools.clear();
		//searchCriteriaTools.addCompanyIdCriteria(this.companyId);
		searchCriteriaTools.addIgualAnd("isDelete","1");
		searchCriteriaTools.addIgualAnd("entiti_id",this.entitiId.toString());
		
		modelSetGetTransaction.setType(Element.class);
		modelSetGetTransaction.setSearchCriteriaTools(this.searchCriteriaTools);
		modelSetGetTransaction.getListValue();
		List<Element> elementList =  (List<Element>) modelSetGetTransaction.getResult();
		
		searchCriteriaTools.clear();
		searchCriteriaTools.addCompanyIdCriteria(this.companyId);
		elementValidation.setSearchCriteriaTools(searchCriteriaTools);
		validation(elementList);
	}
	
	public void updateValidation() throws CustomException {
		searchCriteriaTools.clear();
		//searchCriteriaTools.addCompanyIdCriteria(this.companyId);
		searchCriteriaTools.addIgualAnd("isUpdate","1");
		searchCriteriaTools.addIgualAnd("entiti_id",this.entitiId.toString());
		
		modelSetGetTransaction.setType(Element.class);
		modelSetGetTransaction.setSearchCriteriaTools(this.searchCriteriaTools);
		modelSetGetTransaction.getListValue();
		List<Element> elementList =  (List<Element>) modelSetGetTransaction.getResult();
		
		searchCriteriaTools.clear();
		searchCriteriaTools.addCompanyIdCriteria(this.companyId);
		Long id= (Long) modelSetGet.getValue("id", "id");
		searchCriteriaTools.addCriteria("id", "NoIgual", id.toString(),true);
		elementValidation.setSearchCriteriaTools(searchCriteriaTools);
		validation(elementList);
	}
	
	public void createValidation() throws CustomException {
		searchCriteriaTools.clear();
		//searchCriteriaTools.addCompanyIdCriteria(this.companyId);
		searchCriteriaTools.addIgualAnd("isCreate","1");
		searchCriteriaTools.addIgualAnd("entiti_id",this.entitiId.toString());
		
		modelSetGetTransaction.setType(Element.class);
		modelSetGetTransaction.setSearchCriteriaTools(this.searchCriteriaTools);
		modelSetGetTransaction.getListValue();
		List<Element> elementList =  (List<Element>) modelSetGetTransaction.getResult();
		
		searchCriteriaTools.clear();
		searchCriteriaTools.addCompanyIdCriteria(this.companyId);
		elementValidation.setSearchCriteriaTools(this.searchCriteriaTools);
		validation(elementList);
	}
	
	public void validation(List<Element> elementList) throws CustomException  {
			if (elementList.size()==0) return;
			errorList.clear();
			
			
			
			Object value;
			for(Element element: elementList){
				if (element.getIsRequired()) {
					elementValidation.setError(null);
					elementValidation.setElement(element);
					value = modelSetGet.getValue(element.getIdelement(), element.getLabel());
					elementValidation.setValue(value);
					elementValidation.checkPattern();
					elementValidation.checkIsUnique();
					
					if (elementValidation.getError()!=null) 
						errorList.add(element.getIdelement(), elementValidation.getError(), element.getLabel());	
				}
			}
			if (errorList.size()>0) 
				throw new CustomException("Error en las validaciones",ErrorCode.REST_VALIDATIONS, this.getClass().getSimpleName().toString() , errorList.get()); 
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {		
		this.entityManager = entityManager;
		this.modelSetGetTransaction.setEntityManager(this.entityManager);
		this.elementValidation.setEntityManager(this.entityManager);
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
		this.modelSetGet.setModel(this.model);
		this.elementValidation.setModel(this.model);
	}
}
