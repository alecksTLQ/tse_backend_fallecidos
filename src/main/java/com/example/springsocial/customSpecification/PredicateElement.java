package com.example.springsocial.customSpecification;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springsocial.tools.DateTools;

@SuppressWarnings({"rawtypes","unchecked"})
public class PredicateElement<T>{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Root<?> root;
	private CriteriaBuilder criteriaBuilder;
	private String attribute;
	private String value; 
	private String option;
	private String operator;
	
	private Predicate predicate = null;
	private AttributeAsExpression attributeAsExpression = new AttributeAsExpression();
	private DateTools dateTools= new DateTools();
	
	// SET
	public void setRoot(Root root) {this.root=root;};
	public void setAttribute(String attribute) {this.attribute=attribute;};
	public void setValue(String value) {this.value=value;};
	public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) {this.criteriaBuilder=criteriaBuilder;};
	public void setOption(String option) {this.option=option;};
	public void setOperator(String operator) {this.operator=operator;};
	
	//GET
	public Predicate get() throws Exception {
		attributeAsExpression.setRoot(this.root);
		attributeAsExpression.setCriteriaBuilder(this.criteriaBuilder);
		attributeAsExpression.setAttribute(this.attribute);
		attributeAsExpression.setType();
		attributeAsExpression.setOption(this.option);
		
		this.value=this.value.toLowerCase();
		
		if (this.operator=="and") {
			try { setPredicateAnd();
			}catch(Exception ex) { 
				throw new Exception("Error al asignar filtros"); 
			}
		}else {
			try { setPredicateOr();
			}catch(Exception ex) { 
				throw new Exception("Error al asignar filtros"); 
			}
		}							
		return this.predicate;
	}
	
	//PRIVATE
	private void setPredicateAnd() throws ParseException {
    	switch (this.option) {
        case "Igual":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("string")) 
            	predicate= criteriaBuilder.and(criteriaBuilder.equal((Expression<String>) attributeAsExpression.get(), value));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.and(criteriaBuilder.equal((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.equal((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.equal((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.equal((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("boolean")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.equal((Expression<Boolean>) attributeAsExpression.get(),  value.equals("1") ? true:false ));
        	break;
        case "NoIgual":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("string")) 
            	predicate= criteriaBuilder.and(criteriaBuilder.notEqual((Expression<String>) attributeAsExpression.get(), value));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.and(criteriaBuilder.notEqual((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.notEqual((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.notEqual((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.notEqual((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("boolean")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.notEqual((Expression<Boolean>) attributeAsExpression.get(), value.equals("1") ? true:false ));
        	break;
        case "Contiene":
        	predicate=criteriaBuilder.and(criteriaBuilder.like((Expression<String>) attributeAsExpression.get(),"%"+value+"%" ));
        	break;
        case "Inicia":
        	predicate=criteriaBuilder.and(criteriaBuilder.like(attributeAsExpression.get(), "%"+value));
        	break;
        case "Finaliza":
        	predicate=criteriaBuilder.and(criteriaBuilder.like(attributeAsExpression.get(), value+"%"));
        	break;
        case "Mayor":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.and(criteriaBuilder.greaterThan((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.greaterThan((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.greaterThan((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.greaterThan((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	break;
        case "MayorIgual":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date"))
        		predicate= criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	break;
        case "Menor":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.and(criteriaBuilder.lessThan((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.lessThan((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.lessThan((Expression<Date>) attributeAsExpression.get(), Date.class.cast(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.lessThan((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	break;
        case "MenorIgual":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	break;
        default:
        	predicate= criteriaBuilder.and(criteriaBuilder.equal(attributeAsExpression.get(), value));
            logger.error("Error: Invalid Option: "+option.toString());
            break;
	    }
	}
	
	private void setPredicateOr() throws ParseException {
		switch (this.option) {
        case "Igual":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("string")) 
            	predicate= criteriaBuilder.or(criteriaBuilder.equal((Expression<String>) attributeAsExpression.get(), value));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.or(criteriaBuilder.equal((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.equal((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.equal((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.equal((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("boolean")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.equal((Expression<Boolean>) attributeAsExpression.get(), value.equals("1") ? true:false ));
        	break;
        case "NoIgual":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("string")) 
            	predicate= criteriaBuilder.or(criteriaBuilder.notEqual((Expression<String>) attributeAsExpression.get(), value));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.or(criteriaBuilder.notEqual((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.notEqual((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.notEqual((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.notEqual((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("boolean")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.notEqual((Expression<Boolean>) attributeAsExpression.get(), value.equals("1") ? true:false ));
        	break;
        case "Contiene":
        	predicate=criteriaBuilder.or(criteriaBuilder.like((Expression<String>) attributeAsExpression.get(),"%"+value+"%" ));
        	break;
        case "Inicia":
        	predicate=criteriaBuilder.or(criteriaBuilder.like(attributeAsExpression.get(), "%"+value));
        	break;
        case "Finaliza":
        	predicate=criteriaBuilder.or(criteriaBuilder.like(attributeAsExpression.get(), value+"%"));
        	break;
        case "Mayor":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.or(criteriaBuilder.greaterThan((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.greaterThan((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.greaterThan((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.greaterThan((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	break;
        case "MayorIgual":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	break;
        case "Menor":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.or(criteriaBuilder.lessThan((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.lessThan((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.lessThan((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.lessThan((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	break;
        case "MenorIgual":
        	if (this.attributeAsExpression.getTypeNameLowerCase().equals("integer")) 
            	predicate= criteriaBuilder.or(criteriaBuilder.lessThanOrEqualTo((Expression<Integer>) attributeAsExpression.get(), Integer.valueOf(value) ));	
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("double")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.lessThanOrEqualTo((Expression<Double>) attributeAsExpression.get(), Double.valueOf(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("date")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.lessThanOrEqualTo((Expression<Date>) attributeAsExpression.get(), dateTools.convertISO8601StringToDate(value) ));
        	else if (this.attributeAsExpression.getTypeNameLowerCase().equals("long")) 
        		predicate= criteriaBuilder.or(criteriaBuilder.lessThanOrEqualTo((Expression<Long>) attributeAsExpression.get(), Long.valueOf(value) ));
        	break;
        default:
        	predicate= criteriaBuilder.or(criteriaBuilder.equal(attributeAsExpression.get(), value));
            logger.error("Error: Invalid Option: "+option.toString());
            break;
	    }
	}
}