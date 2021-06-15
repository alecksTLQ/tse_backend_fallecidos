package com.example.springsocial.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springsocial.model.CabeceraFolioModell;
import com.example.springsocial.model.IdCabecera;

@Repository
@Transactional
public interface CabeceraFolioRepository extends 
CrudRepository<CabeceraFolioModell, Integer>,
JpaRepository<CabeceraFolioModell, Integer>
{
	@Query(value="SELECT MAX(NROFOLIO) FROM CABECERAFOLIO WHERE USRACTUA=:usuario AND AÑOFOLIO=:año", nativeQuery= true)
	Integer selectMaxFolio(@Param("usuario") String usuario, @Param("año") Integer año);
	
	@Query(value="SELECT LINEA FROM CABECERAFOLIO WHERE NROFOLIO=:folio AND AÑOFOLIO=:año", nativeQuery= true)
	Integer selectLineaFolio(@Param("folio") Integer folio, @Param("año") Integer año);
	
	@Query(value="select nrofolio.nextval from dual", nativeQuery= true)
	Integer selectNoFolio();
}

