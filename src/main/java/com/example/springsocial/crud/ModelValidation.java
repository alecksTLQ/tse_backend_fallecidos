package com.example.springsocial.crud;

import java.util.List;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.model.Element;
import com.example.springsocial.model.Entiti;
import com.example.springsocial.repository.ElementRepository;
import com.example.springsocial.repository.EntitiRepository;
import com.example.springsocial.tools.SearchCriteriaTools;

@SuppressWarnings({"rawtypes","unchecked"})
public class ModelValidation {
	private Object model;
	private String modelName;
	private Object repository;
	
	private EntitiRepository entitiRepository;
	private ElementRepository elementRepository;

	private SearchCriteriaTools searchCriteriaTools = new SearchCriteriaTools();
	private ElementValidation elementValidation= new ElementValidation();
	private ErrorList errorList = new ErrorList();
	private ModelSetGet modelSetGet= new ModelSetGet();
	private Long entitiId;
	private Long companyId;
	
	
	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
		elementValidation.setModel(this.model);
		modelSetGet.setModel(model);
	}

	public Object getRepository() {
		return repository;
	}

	public void setRepository(Object repository) {
		this.repository = repository;
		elementValidation.setRepository(this.repository);
	}
	
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
		elementValidation.setCompanyId(this.companyId);
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
		Entiti entiti = this.entitiRepository.findAllByName(this.modelName);
		if (entiti==null)  this.entitiId=Long.parseLong("0"); 
		else this.entitiId=entiti.getId() ;  
	}
	
	public EntitiRepository getEntitiRepository() {
		return entitiRepository;
	}

	public void setEntitiRepository(EntitiRepository entitiRepository) {
		this.entitiRepository = entitiRepository;
	}

	public ElementRepository getElementRepository() {
		return elementRepository;
	}

	public void setElementRepository(ElementRepository elementRepository) {
		this.elementRepository = elementRepository;
	}
	
	public void deleteValidation() throws CustomException {
		List<Element> elementList =  elementRepository.findAllByEntitiIdAndIsDelete(this.entitiId,true);
		searchCriteriaTools.clear();
		searchCriteriaTools.addCompanyIdCriteria(this.companyId);
		elementValidation.setSearchCriteriaTools(searchCriteriaTools);
		validation(elementList);
	}
	
	public void updateValidation() throws CustomException {
		List<Element> elementList =  elementRepository.findAllByEntitiIdAndIsUpdate(this.entitiId,true);
		
		searchCriteriaTools.clear();
		searchCriteriaTools.addCompanyIdCriteria(this.companyId);
		
		Long id= (Long) modelSetGet.getValue("id", "id");
		searchCriteriaTools.addCriteria("id", "NoIgual", id.toString(),true);
		elementValidation.setSearchCriteriaTools(searchCriteriaTools);
		validation(elementList);
	}
	
	public void createValidation() throws CustomException {
		List<Element> elementList =  elementRepository.findAllByEntitiIdAndIsCreate(this.entitiId,true);
		searchCriteriaTools.clear();
		searchCriteriaTools.addCompanyIdCriteria(this.companyId);
		elementValidation.setSearchCriteriaTools(searchCriteriaTools);
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
			
			if (errorList.size()>0) {
				throw new CustomException("Error en las validaciones",ErrorCode.REST_VALIDATIONS, this.getClass().getSimpleName().toString() , errorList.get());
			}	
		 
			
	}
}
