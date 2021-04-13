package com.example.springsocial.tools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings("serial")
public class JPAcustomSpecification<T> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	public Specification<T> getSpecification(Optional<String> searchCriteria, Optional<String> orderCriteria) {
		JSONArray searchCriteriaArray=null, orderCriteriaArray=null;
		try {
			if (searchCriteria.isPresent() && !searchCriteria.get().equals("undefined"))	searchCriteriaArray = new JSONArray(searchCriteria.get());
			if (orderCriteria.isPresent() && !orderCriteria.get().equals("undefined"))  orderCriteriaArray = new JSONArray(orderCriteria.get());
			return getSpecification(searchCriteriaArray,orderCriteriaArray);	
		}catch(Exception ex) {
			logger.error(ex.toString());
		}
		return null;
		
		 
	}
	
	public Specification<T> getSpecification(final JSONArray searchCriteriaArray, final JSONArray orderCriteriaArray) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {  
				if (orderCriteriaArray!=null) {
					List<Order> orderList = getOrderList(orderCriteriaArray, root, builder);
					query.orderBy(orderList);	
				}
				
				if (searchCriteriaArray!=null) {
					JSONArray searchCriteriaAndOperator= getSearchCriteriaByOperator(searchCriteriaArray,"and"); 
					JSONArray searchCriteriaOrOperator = getSearchCriteriaByOperator(searchCriteriaArray,"or");
					
					List<Predicate> predicatesListAndOperator = getPredicateList(root,builder, searchCriteriaAndOperator);
					List<Predicate> predicatesListOrOperator = getPredicateList(root,builder, searchCriteriaOrOperator );
					
					Predicate predicatesOrOperator =builder.or(predicatesListOrOperator.toArray(new Predicate[predicatesListOrOperator .size()]));
					Predicate predicatesAndOperator 	=builder.and(predicatesListAndOperator.toArray(new Predicate[predicatesListAndOperator.size()]));
					
					if (predicatesListOrOperator.size()==0)  	return 	builder.and(predicatesAndOperator);	
					else 									return 	builder.and(predicatesAndOperator,predicatesOrOperator);
					
				}else
					return null;
			}
		};
	}
    
	private JSONArray getSearchCriteriaByOperator(JSONArray searchCriteria, String operator) {
		JSONArray result= new JSONArray();
		JSONObject filterElement;
		try {
			for(int index=0; index < searchCriteria.length(); index++) {
				filterElement = (JSONObject) searchCriteria.get(index);
				if (filterElement.has("operator")){
					if (filterElement.get("operator").equals(operator)) {
						result.put(filterElement);
					}	
				}
			}	
		}catch(Exception ex) {
			logger.error(ex.toString());
		}
		
		return result;
	}
	 
	
    private List<Order> getOrderList(JSONArray orderCriteria, Root<T> root, CriteriaBuilder builder){
    	List<Order> orderList = new LinkedList<>();
    	JSONObject filterElement;
 		String attribute="",option="";
 		try {
			for(int index=0; index < orderCriteria.length(); index++) {
				
	        	filterElement = (JSONObject) orderCriteria.get(index);
	        	attribute = filterElement.get("id").toString();
				option = filterElement.get("option").toString();
				if (option.equals("asc"))
					orderList.add(builder.asc(root.get(attribute)));
				else 			 
					orderList.add(builder.desc(root.get(attribute)));
	        }	
		}catch(Exception ex) {
			logger.error(ex.toString());
		}
    	return orderList;
    }
    
    public Predicate getPredicateList(Optional<String> searchCriteria, Optional<String> orderCriteria, Root<T> root,   CriteriaBuilder builder ) {
		JSONArray searchCriteriaArray=null, orderCriteriaArray=null;
		try {
			if (searchCriteria.isPresent() && !searchCriteria.get().equals("undefined"))	searchCriteriaArray = new JSONArray(searchCriteria.get());
			if (orderCriteria.isPresent() && !orderCriteria.get().equals("undefined"))  orderCriteriaArray = new JSONArray(orderCriteria.get());
			
			JSONArray searchCriteriaAndOperator= getSearchCriteriaByOperator(searchCriteriaArray,"and"); 
			JSONArray searchCriteriaOrOperator = getSearchCriteriaByOperator(searchCriteriaArray,"or");
			
			List<Predicate> predicatesListAndOperator = getPredicateList(root,builder, searchCriteriaAndOperator);
			List<Predicate> predicatesListOrOperator = getPredicateList(root,builder, searchCriteriaOrOperator );
			
			Predicate predicatesOrOperator =builder.or(predicatesListOrOperator.toArray(new Predicate[predicatesListOrOperator .size()]));
			Predicate predicatesAndOperator 	=builder.and(predicatesListAndOperator.toArray(new Predicate[predicatesListAndOperator.size()]));
			
			if (predicatesListAndOperator.size()>0 && predicatesListOrOperator.size()>0)
				return 	builder.and(predicatesAndOperator,predicatesOrOperator);
			else if (predicatesListAndOperator.size()>0 && predicatesListOrOperator.size()==0)
				return 	builder.and(predicatesAndOperator);	
			else if (predicatesListAndOperator.size()==0 && predicatesListOrOperator.size()>0)
				return 	builder.and(predicatesOrOperator);
			else
				return null;
				
		}catch(Exception ex) {
			logger.error(ex.toString());
		}
		return null;
		
		 
	}
    
    private List<Predicate> getPredicateList(Root<?> root, CriteriaBuilder criteriaBuilder, JSONArray searchCriteriaArray){
        List<Predicate> predicates = new ArrayList<>();
        JSONObject filterElement;
		String attribute="",option="",value="";
		
		try {
			for(int index=0; index < searchCriteriaArray.length(); index++) {
	        	filterElement = (JSONObject) searchCriteriaArray.get(index);
	        	attribute = filterElement.get("id").toString();
				option = filterElement.get("option").toString();
				value = filterElement.get("value").toString();
	        	predicates.add(getPredicate(root,criteriaBuilder,option,attribute,value));
	        }	
		}catch(Exception ex) {
			logger.error(ex.toString());
		}
        
        return predicates;
    }
    
    @SuppressWarnings("unchecked")
	private Predicate getPredicate(Root<?> root, CriteriaBuilder criteriaBuilder,String option, String attribute, String value) {
    	Predicate predicate = null;
    	value=value.toLowerCase();
    	switch (option) {
        case "Igual":
        	predicate= criteriaBuilder.and(criteriaBuilder.equal(getAtribute(attribute, root, "String",criteriaBuilder), value)) ;
            break;
        case "NoIgual":
        	predicate= criteriaBuilder.and(criteriaBuilder.notEqual(getAtribute(attribute, root, "String",criteriaBuilder), value)) ;
            break;
        case "Contiene":
        	predicate=criteriaBuilder.or(criteriaBuilder.like((Expression<String>) getAtribute(attribute, root, "String",criteriaBuilder), "%"+value+"%"));
        	break;
        case "Inicia":
        	predicate=criteriaBuilder.or(criteriaBuilder.like((Expression<String>) getAtribute(attribute, root, "String",criteriaBuilder), "%"+value));
        	break;
        case "Finaliza":
        	predicate=criteriaBuilder.or(criteriaBuilder.like((Expression<String>) getAtribute(attribute, root, "String",criteriaBuilder), value+"%"));
        	break;
        case "Mayor":
        	predicate= criteriaBuilder.or(criteriaBuilder.greaterThan((Expression<Integer>) getAtribute(attribute, root, "Double",criteriaBuilder), Integer.valueOf(value) ));
        	break;
        case "MayorIgual":
        	predicate= criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo((Expression<Integer>) getAtribute(attribute, root, "Double",criteriaBuilder), Integer.valueOf(value) ));
        	break;
        case "Menor":
        	predicate= criteriaBuilder.or(criteriaBuilder.lessThan((Expression<Integer>) getAtribute(attribute, root, "Double",criteriaBuilder), Integer.valueOf(value) ));
        	break;
        case "MenorIgual":
        	predicate= criteriaBuilder.or(criteriaBuilder.lessThanOrEqualTo((Expression<Integer>) getAtribute(attribute, root, "Double",criteriaBuilder), Integer.valueOf(value) ));
        	break;
        default:
        	predicate= criteriaBuilder.or(criteriaBuilder.equal(getAtribute(attribute, root, "String",criteriaBuilder), value));
            logger.error("Error: Invalid Option: "+option.toString());
            break;
	    }
    	return predicate;
    }
    
    private Expression<?> getAtribute(String attribute, Root<?> root, String type, CriteriaBuilder criteriaBuilder ) {
    	String[] parts = attribute.split("[.]");
    	if (parts.length==0) return root;
    	
    	if (type=="String") {
    		if  (parts.length==1) return criteriaBuilder.lower(root.<T>get(attribute).as(String.class));
    		else if (parts.length==2) return criteriaBuilder.lower(root.<T>get(parts[0]).<T>get(parts[1]).as(String.class));
    		else if (parts.length==3) return criteriaBuilder.lower(root.<T>get(parts[0]).<T>get(parts[1]).<T>get(parts[2]).as(String.class));
    		else return root;
    	}if (type=="Integer") {
    		if  (parts.length==1) return root.<String>get(attribute);
    		else if (parts.length==2) return root.<String>get(parts[0]).get(parts[1]);
    		else if (parts.length==3) return root.<Integer>get(parts[0]).<Integer>get(parts[1]).<Integer>get(parts[2]);
    		else return root;
    	}else return root;
    }
}






















