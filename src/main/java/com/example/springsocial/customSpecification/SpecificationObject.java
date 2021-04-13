package com.example.springsocial.customSpecification;

import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings({"rawtypes","unchecked"})
public class SpecificationObject<T> {
	private Specification specification;
	private PredicateObject predicateObject;
	
	private Optional<String> searchCriteria;
	private Optional<String> orderCriteria;
	 	
	//SET
	public void setSearchCriteria (Optional<String> searchCriteria) { this.searchCriteria= searchCriteria;};
	public void setOrderCriteria (Optional<String> orderCriteria) { this.orderCriteria=orderCriteria;};
	
	//GET
	public Specification<T> get() {
		setSpecification();
		return this.specification;
	}
	
	//PRIVATE
	public void setSpecification() {
		this.specification=new Specification<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {  
				OrderObject orderObject = new OrderObject();
				orderObject.setRoot(root);
				orderObject.setQuery(query);
				orderObject.setCriteriaBuilder(criteriaBuilder);
				orderObject.setOrderCriteria(orderCriteria);
				query=orderObject.get();
				
				predicateObject= new PredicateObject();
				predicateObject.setCriteriaBuilder(criteriaBuilder);
				predicateObject.setRoot(root);
				predicateObject.setSearchCriteria(searchCriteria);
				return predicateObject.get();
				 
			}
		};
		
		
	}
}


