package com.example.springsocial.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springsocial.model.CabeceraFolioModell;
import com.example.springsocial.model.DetalleFolio;

@Repository
@Transactional
public interface DetalleFolioRepository extends
CrudRepository<DetalleFolio, Integer>,
JpaRepository<DetalleFolio, Integer>
{
	@Query(value ="SELECT * FROM DETALLEFOLIO WHERE NROBOLETA =:nroboleta OR CUI=:cui OR NROORDEN =:nroorden OR NROREGIST =:nroregist", nativeQuery = true)
	List<DetalleFolio> consultarCaptacion(@Param("nroboleta") Integer nroboleta, @Param("cui") Long cui, @Param("nroorden") String nroorden, @Param("nroregist") Integer nroregist);
	
	@Query(value="select IDDETALLE.nextval from dual", nativeQuery= true)
	Integer selectNoFolio();
}
