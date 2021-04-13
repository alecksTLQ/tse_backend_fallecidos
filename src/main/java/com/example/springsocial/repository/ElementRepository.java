package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.generic.CrudCustom;
import com.example.springsocial.model.Element;

@Repository
@Transactional
public interface ElementRepository <T> 
	extends CrudRepository<Element, Integer>, 
			PagingAndSortingRepository<Element, Integer>, 
			JpaSpecificationExecutor<Element>, 
			JpaRepository<Element, Integer>, 
			CrudCustom<Element> {
	List<Element> findAllByEntitiId(Long entitiId);
	List<Element> findAllByEntitiIdAndIsCreate(Long entitiId, Boolean isCreate);
	List<Element> findAllByEntitiIdAndIsUpdate(Long entitiId, Boolean isUpdate);
	List<Element> findAllByEntitiIdAndIsDelete(Long entitiId, Boolean isDelete);
}
