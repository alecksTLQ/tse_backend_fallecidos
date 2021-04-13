package com.example.springsocial.tools;

import java.util.Optional;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SearchCriteriaTools {
	Optional<String> searchCriteria=Optional.empty();
	
	public void setSearchCriteria(Optional<String> searchCriteria) {
		this.searchCriteria= searchCriteria;
	}
	
	public Optional<String> getSearchCriteria() {
		return this.searchCriteria;
	}
	
	private String createArray(String id, String option, String value, String operator) {
		return "{'id':'"+id+ "','option':'"+option+"' ,value:'"+value+"','operator':'"+operator+"'}";
	}
	
	public void addCriteria(String id, String option, String value, Boolean isAndOperator) {
		String operator="or";
		if (isAndOperator) operator="and";
		
		if (this.searchCriteria.isPresent()) {
			if (!this.searchCriteria.get().toString().equals("[]")) {
				String searchCriteriaString =  this.searchCriteria.get().trim().substring(0, this.searchCriteria.get().length()-1);
				this.searchCriteria=  Optional.of(searchCriteriaString+ ","+createArray(id,option,value,operator)+"]");
			}else	this.searchCriteria=  Optional.of("["+createArray(id,option,value,operator)+"]");
			
		}else {
			this.searchCriteria=  Optional.of("["+createArray(id,option,value,operator)+"]");
		}
	}
	
	

	
	public void addCriteria(String id, String option, String value) {
		addCriteria(id,option,value,false);
	}
	
	public void addCompanyIdCriteria(Long id) {
		addCriteria("companyId","Igual",id.toString(),true);
	}
	
	public void addIgualAnd(String id, String value) {
		addCriteria(id,"Igual",value.toString(),true);
	}
	

	
	
	public void clear() {
		this.searchCriteria=  Optional.empty();
	}
	
	public Object getById(String id){
		try {
			OptionalTools optionalTools = new OptionalTools();
			optionalTools.setValue(this.searchCriteria);
			optionalTools.convertJsonArray();
			for (int index=0; index < optionalTools.getJsonArray().length(); index++) {
				JSONObject item = optionalTools.getJsonArray().getJSONObject(index);
				if (item.get("id").equals(id)) return item.get("value");
			}
			return "";
		}catch(Exception exception) {
			return null;
		}
		
	}
	
	
}
