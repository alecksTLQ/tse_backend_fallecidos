package com.example.springsocial.customSpecification;

import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.codehaus.jettison.json.JSONArray;

import com.example.springsocial.tools.OptionalTools;

public class PredicateObject<T> {
	private JSONArray searchCriteria;
	
	private JSONArray searchCriteriaAndOperator;
	private JSONArray searchCriteriaOrOperator; 
	
	private List<Predicate> predicateListAndOperator;
	private List<Predicate> predicateListOrOperator;
	
	private PredicateList predicateList;
	private Predicate predicateResponse;
	
	private Root<T> root;
	private CriteriaBuilder criteriaBuilder; 
	private OptionalTools optionalTools = new OptionalTools();
	public PredicateObject() {}
	
	// SET 
	public void setRoot(Root<T> root) { this.root=root;};
	public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) { this.criteriaBuilder=criteriaBuilder;};
	public void setSearchCriteria (Optional<String> searchCriteria) {
		optionalTools.setValue(searchCriteria);
		optionalTools.convertJsonArray();
		this.searchCriteria= optionalTools.getJsonArray();
	};
	// GET 
	public Predicate get() {
		splitSearchCriteriaAndOr();
		setPredicateListsAndOr();
		setPredicateList();
		return this.predicateResponse;
	}
	
	private void setPredicateList() {
		Predicate predicatesOrOperator =criteriaBuilder.or(this.predicateListOrOperator.toArray(new Predicate[this.predicateListOrOperator.size()]));
		Predicate predicatesAndOperator 	=criteriaBuilder.and(this.predicateListAndOperator.toArray(new Predicate[this.predicateListAndOperator.size()]));
		
		if (predicateListAndOperator.size()>0 && predicateListOrOperator.size()>0)
			predicateResponse= 	criteriaBuilder.and(predicatesAndOperator,predicatesOrOperator);
		else if (this.predicateListAndOperator.size()>0 && predicateListOrOperator.size()==0)
			predicateResponse=criteriaBuilder.and(predicatesAndOperator);	
		else if (predicateListAndOperator.size()==0 && predicateListOrOperator.size()>0)
			predicateResponse= criteriaBuilder.and(predicatesOrOperator);
		else
			predicateResponse=null;
	}
	
	
	// PRIVATE
	private void setPredicateListsAndOr() {
		predicateList= new PredicateList();
		predicateList.setRoot(this.root);
		predicateList.setCriteriaBuilder(this.criteriaBuilder);
		
		predicateList.setArrayValue(this.searchCriteriaAndOperator);
		predicateList.setOperator("and");
		predicateListAndOperator =predicateList.get();
		
		
		predicateList= new PredicateList();
		predicateList.setRoot(this.root);
		predicateList.setCriteriaBuilder(this.criteriaBuilder);
		
		predicateList.setArrayValue(this.searchCriteriaOrOperator);
		predicateList.setOperator("or");
		predicateListOrOperator =predicateList.get();
	}
	
	private void splitSearchCriteriaAndOr() {
		JSONArrayOperator jsonArrayOperator = new JSONArrayOperator();
		jsonArrayOperator.setArrayValue(this.searchCriteria);
		jsonArrayOperator.setOperator("and");
		this.searchCriteriaAndOperator=jsonArrayOperator.get();
		
		jsonArrayOperator.setOperator("or");
		this.searchCriteriaOrOperator=jsonArrayOperator.get();
	}
}









