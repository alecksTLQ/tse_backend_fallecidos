package com.example.springsocial.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.generic.CrudCustom;
import com.example.springsocial.model.ImagesRenapModel;

@Repository
@Transactional
public interface ImagesRenapRepository  
	extends CrudRepository<ImagesRenapModel, Integer>, 
			PagingAndSortingRepository<ImagesRenapModel, Integer>, 
			JpaSpecificationExecutor<ImagesRenapModel>, 
			JpaRepository<ImagesRenapModel, Integer>, 
			CrudCustom<ImagesRenapModel> {	
	
	ImagesRenapModel findByCui(String cui);
	 boolean existsBycui(String cui);
}
