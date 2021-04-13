package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.example.springsocial.generic.CrudCustom;
import com.example.springsocial.model.Entiti;

@Repository
@Transactional
public interface EntitiRepository  <T> 
		extends CrudRepository<Entiti, Integer>, 
				PagingAndSortingRepository<Entiti, Integer>, 
				JpaSpecificationExecutor<Entiti>, 
				JpaRepository<Entiti, Integer>, 
				CrudCustom<Entiti> {
	List<Entiti> findAllById(Long id);
	Entiti findAllByName(String name);
}
