package com.example.springsocial.generic;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.example.springsocial.customSpecification.SpecificationObject;
import com.example.springsocial.tools.SearchCriteriaTools;

@SuppressWarnings({"unchecked","rawtypes"})
public interface CrudCustom<T> extends JpaSpecificationExecutor<T>  {
	SpecificationObject specificationObject = new SpecificationObject();
	SearchCriteriaTools searchCriteriaTools = new SearchCriteriaTools();
	
	default
	List<T> findAll(Optional<String> searchCriteria, Optional<String> orderCriteria) {
		specificationObject.setSearchCriteria(searchCriteria);
		specificationObject.setOrderCriteria(orderCriteria);
		return findAll(specificationObject.get());

	}
	default
	Page<T> page(Optional<String> searchCriteria, Optional<String> orderCriteria, Integer pageNumber, Integer pageSize) {
		specificationObject.setSearchCriteria(searchCriteria);
		specificationObject.setOrderCriteria(orderCriteria);
		return findAll(specificationObject.get(),PageRequest.of(pageNumber, pageSize));
	}
	
	default
	List<T> findAll(Optional<String> searchCriteria, Optional<String> orderCriteria, Long companyId) {
		searchCriteriaTools.clear();
		searchCriteriaTools.setSearchCriteria(searchCriteria);
    	searchCriteriaTools.addCompanyIdCriteria(companyId);
    	searchCriteria =  searchCriteriaTools.getSearchCriteria();

    	specificationObject.setSearchCriteria(searchCriteria);
		specificationObject.setOrderCriteria(orderCriteria);
		
		return findAll(specificationObject.get());

	}
	
	default
	Page<T> page(Optional<String> searchCriteria, Optional<String> orderCriteria, Integer pageNumber, Integer pageSize, Long companyId) {
		searchCriteriaTools.clear();
		searchCriteriaTools.setSearchCriteria(searchCriteria);
    	searchCriteriaTools.addCompanyIdCriteria(companyId);
    	searchCriteria =  searchCriteriaTools.getSearchCriteria();
    	
    	specificationObject.setSearchCriteria(searchCriteria);
		specificationObject.setOrderCriteria(orderCriteria);
		
		return findAll(specificationObject.get(),PageRequest.of(pageNumber, pageSize));
	}

}
