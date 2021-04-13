package com.example.springsocial.crud;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.beanutils.PropertyUtils;
import com.example.springsocial.generic.GenericClass;
import com.example.springsocial.model.Element;
import com.example.springsocial.tools.SearchCriteriaTools;

@SuppressWarnings({"rawtypes","unchecked"})
public class ElementValidation{
	private GenericClass genericClass;
	private Element element;
	private Object value;
	private Object model;
	private String error=null;
	private Long companyId;
	private Object repository;
	private SearchCriteriaTools searchCriteriaTools;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	public SearchCriteriaTools getSearchCriteriaTools() {
		return searchCriteriaTools;
	}

	public void setSearchCriteriaTools(SearchCriteriaTools searchCriteriaTools) {
		this.searchCriteriaTools = searchCriteriaTools;
	}

	public Object getRepository() {
		return repository;
	}

	public void setRepository(Object repository) {
		this.repository = repository;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public Object getValue() {
		return value;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public void setValue( Object value) {			
		this.value = value;
	}
	
	public void checkPattern() {
		if (this.error!=null) return;
		String pattern = "";
		if (element.getPattern().length()>0) {
			if (this.value==null) {
				this.error=this.element.getPatternMessage();
			}else {
				Boolean matches=false;
				pattern= element.getPattern().replace("\\\\", "\\");
				if (this.value instanceof BigDecimal) 
					matches= new BigDecimal(((BigDecimal) this.value).longValueExact()).toString().matches(pattern);
				else if (this.value instanceof Date)
					matches= formatter.format(this.value).toString().matches(pattern);
				else 
					matches= this.value.toString().matches(pattern);
				
				if (matches==false)
					this.error=this.element.getPatternMessage();
				
			}
		}
	}
	
	
	
	public void checkIsUnique() {
		try {
			if (this.error!=null) return;
			if (element.getIsUnique()) {
								
				SearchCriteriaTools searchCriteriaElement = new SearchCriteriaTools();
				searchCriteriaElement.setSearchCriteria(this.searchCriteriaTools.getSearchCriteria());
				searchCriteriaElement.addCriteria(element.getIdelement(), "Igual", PropertyUtils.getProperty(this.model, this.element.getIdelement()).toString().trim(), true);
				
				genericClass= new GenericClass(repository,"findAll",new Object [] {searchCriteriaElement.getSearchCriteria(),Optional.empty()});
				genericClass.executeMethod();
				if (genericClass.getIsError()==true){
					this.error="Error al obtener el contenido";
				}else {
					//RestResponse responseFindAll = ( RestResponse) genericClass.getResult();
					List<Object> responseObject=(List<Object>) genericClass.getResult();
					if (responseObject.size()>0) {
						this.error="Contenido duplicado";
					}
				}
			}
		}catch(Exception ex) {
			this.error="Error en la operación";
		}
		
	}


}
