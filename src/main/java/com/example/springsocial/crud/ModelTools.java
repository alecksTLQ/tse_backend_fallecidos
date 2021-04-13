package com.example.springsocial.crud;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.model.Element;
import com.example.springsocial.repository.ElementRepository;
import com.example.springsocial.repository.EntitiRepository;
import com.example.springsocial.tools.DateTools;

@SuppressWarnings({"rawtypes","unchecked"})
public class ModelTools<T> {
	private Object model;
	private String name;
	private Long entitiId;
	private Long companyId;
	private Long userId;
	
	private ElementRepository elementRepository;
	private EntitiRepository entitiRepository;
	private Object repository;

	private ErrorList errorList = new ErrorList();
	private ElementValidation elementValidation=new ElementValidation();
	private ModelSetGet modelSetGet= new ModelSetGet();
	private DateTools dateTools = new DateTools();
		
	public Object getRepository() {
		return repository;
	}

	public void setRepository(Object repository) {
		this.repository = repository;
	}
	
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ElementRepository getElementRepository() {
		return elementRepository;
	}

	public void setElementRepository(ElementRepository elementRepository) {
		this.elementRepository = elementRepository;
	}

	public EntitiRepository getEntitiRepository() {
		return entitiRepository;
	}

	public void setEntitiRepository(EntitiRepository entitiRepository) {
		this.entitiRepository = entitiRepository;
	}
	
	public void setValuesFromObject(Object paramObject) throws CustomException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		ParamToModel paramToModel =  new ParamToModel();
		paramToModel.setElementObject(this.model);
		paramToModel.setParamObject(paramObject);
		paramToModel.setParamsToElement();
	}
	
	public void setUpdatedAtToModel() throws CustomException{
		this.modelSetGet.setValue("setUpdatedAt","fecha de actualización",dateTools.get_CurrentDate());		
	}
	
	public void setCompanyIdToModel() throws CustomException{
		this.modelSetGet.setValue("setCompanyId","código de empresa",this.companyId);
	}
	
	public void setCreatedAtToModel() throws CustomException{
		this.modelSetGet.setValue("setCreatedAt","fecha de creación",dateTools.get_CurrentDate());
	}
	
	public void setCreatedByToModel() throws CustomException{
		this.modelSetGet.setValue("setCreatedBy","usuario de creación", this.userId);
	}
	
	public void setUpdatedByToModel() throws CustomException{
		this.modelSetGet.setValue("setUpdatedBy","usuario de actualización",this.userId);
	}
	
	
	private void setEntitiId() {
		if (this.entitiId==null)
			this.entitiId= this.entitiRepository.findAllByName(this.name).getId();
	}
	
	
	public void createValidations() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, CustomException {
		this.setEntitiId();
		List<Element> elementList =  elementRepository.findAllByEntitiId(this.entitiId);
		if (elementList.size()==0) return;
		
		elementValidation.setCompanyId(this.companyId);
		elementValidation.setRepository(this.repository);
		elementValidation.setModel(this.model);
		errorList.clear();
		modelSetGet.setModel(model);
		Object value;
		for(Element element: elementList){
			elementValidation.setError(null);
			elementValidation.setElement(element);
			value = modelSetGet.getValue(element.getIdelement(), element.getLabel());
			elementValidation.setValue(value);
			elementValidation.checkPattern();
			elementValidation.checkIsUnique();
			
			if (elementValidation.getError()!=null)
				errorList.add(element.getIdelement(), elementValidation.getError(), element.getLabel());
		}
		
		if (errorList.size()>0) {
			//throw new CustomException("Error en las validaciones",ErrorCode.REST_CREATE, this.getClass().getSimpleName().toString() , errorList.get());
		}
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
	
}
