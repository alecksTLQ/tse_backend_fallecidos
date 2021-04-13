package com.example.springsocial.customSpecification;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class OrderObject<T>{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private List<Order> orderList;
		
	private JSONArray orderCriteria;
	private Root<T> root;
	private CriteriaBuilder criteriaBuilder;
	private CriteriaQuery<?> query;
	
	//SET
	public void setRoot(Root<T> root) { this.root=root;};
	
	public void setQuery(CriteriaQuery query) {this.query=query;};
	public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) { this.criteriaBuilder=criteriaBuilder;};
	public void setOrderCriteria (Optional<String> orderCriteria) { this.orderCriteria= convertOptionalToJSONArray(orderCriteria);};
	
	// GET
	public CriteriaQuery get(){
		this.orderList= new LinkedList<>();
		setOrderCriteria();
		setOrderListToQuery();
		return this.query;
	};
	
	//PRIVATE
	private void setOrderListToQuery() {
		if (this.orderList.size()>0)
			this.query.orderBy(this.orderList);	
	}
	
	private void setOrderCriteria() {
		if (orderCriteria==null) return;
		if (orderCriteria.length()==0) return;
    	JSONObject filterElement;
 		String attribute="",option="";
 		String[] attributeArray;
 		try {
			for(int index=0; index < orderCriteria.length(); index++) {
				
	        	filterElement = (JSONObject) orderCriteria.get(index);
	        	attribute = filterElement.get("id").toString();
	        	attributeArray = attribute.split("[.]");
				option = filterElement.get("option").toString();
				if (attributeArray.length==1) {
					if (option.equals("asc")) this.orderList.add(this.criteriaBuilder.asc(root.get(attribute)));
					else this.orderList.add(this.criteriaBuilder.desc(root.get(attribute)));	
				}else if (attributeArray.length==2) {
					if (option.equals("asc")) this.orderList.add(this.criteriaBuilder.asc(root.<String>get(attributeArray[0]).get(attributeArray[1])));
					else this.orderList.add(this.criteriaBuilder.desc(root.<String>get(attributeArray[0]).get(attributeArray[1])));
				}else if (attributeArray.length==3) {
					if (option.equals("asc")) this.orderList.add(this.criteriaBuilder.asc(root.<String>get(attributeArray[0]).<String>get(attributeArray[1]).<String>get(attributeArray[2])));
					else this.orderList.add(this.criteriaBuilder.desc(root.<String>get(attributeArray[0]).<String>get(attributeArray[1]).<String>get(attributeArray[2])));
					
				}
				
	        }	
		}catch(Exception ex) {
			logger.error(ex.toString());
		}
	}
	
	private JSONArray convertOptionalToJSONArray(Optional<String> value) {
		try {
			 return (value.isPresent() && !value.get().equals("undefined"))	?  new JSONArray(value.get()) : null;
		}catch(Exception ex) {
			return null;
		}
		
	}
	
}