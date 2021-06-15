package com.example.springsocial.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springsocial.model.CabeceraFolioModell;
import com.example.springsocial.model.DetalleFolio;

@Repository
@Transactional
public interface DetalleFolioRepository extends
CrudRepository<DetalleFolio, Integer>,
JpaRepository<DetalleFolio, Integer>
{

	
	@Query(value="select IDDETALLE.nextval from dual", nativeQuery= true)
	Integer selectNoFolio();
}
