package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.generic.CrudCustom;
import com.example.springsocial.model.HistoricoAfiliacionModel;

@Repository
@Transactional
public interface HistorialAfiliacionRepository  <T>  
	extends CrudRepository<HistoricoAfiliacionModel, Integer>, 
			PagingAndSortingRepository<HistoricoAfiliacionModel, Integer>, 
			JpaSpecificationExecutor<HistoricoAfiliacionModel>, 
			JpaRepository<HistoricoAfiliacionModel, Integer>, 
			CrudCustom<HistoricoAfiliacionModel> {	
	
	//HistoricoAfiliacionModel findByCui(String cui);
	//List<HistoricoAfiliacionModel> findAllByCui(String cui);
    //boolean existsByCui(String cui);

}
