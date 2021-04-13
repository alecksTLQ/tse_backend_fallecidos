package com.example.springsocial.customSpecification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class PredicateList{
	private Logger logger = LoggerFactory.getLogger(this.getClass());	
	private PredicateElement predicateElement= new PredicateElement();
	private List<Predicate> predicateList = new ArrayList<>();
	 
	private Root<?> root;
	private CriteriaBuilder criteriaBuilder;
	private JSONArray arrayValue;
	private String operator;
	
	//SET
	public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) {this.criteriaBuilder=criteriaBuilder;};
	public void setRoot(Root root) {this.root=root;};
	public void setArrayValue(JSONArray arrayValue) { this.arrayValue=arrayValue;}
	public void setOperator(String operator) {this.operator=operator;};
	
	//GET
	public List<Predicate> get(){
		predicateElement.setRoot(this.root);
		predicateElement.setCriteriaBuilder(this.criteriaBuilder);
		predicateElement.setOperator(this.operator);
		setPredicateList();
		return this.predicateList;
	}
	
	private void setPredicateList() {
		String attributeItem="",optionItem="",valueItem="",operatorItem="";
		JSONObject filterElement;
		try {
			for(int index=0; index < this.arrayValue.length(); index++) {
	        	filterElement = (JSONObject) this.arrayValue.get(index);
	        	attributeItem = filterElement.get("id").toString();
				optionItem = filterElement.get("option").toString();
				valueItem = filterElement.get("value").toString();
				operatorItem= filterElement.get("operator").toString();
				if (operatorItem.equals(operator)) {
					predicateElement.setAttribute(attributeItem);
					predicateElement.setOption(optionItem);
					predicateElement.setValue(valueItem.toLowerCase());
					predicateList.add(predicateElement.get());	
				}
	        }	
		}catch(Exception ex) {
			logger.error(ex.toString());
		}
	}
	
}