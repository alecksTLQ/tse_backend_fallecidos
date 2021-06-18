package com.example.springsocial.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springsocial.model.CabeceraFolioModel;

@Repository
@Transactional
public interface CabeceraFolioRepository2 extends 
CrudRepository<CabeceraFolioModel, Integer>,
JpaRepository<CabeceraFolioModel, Integer>{
	
	/*
	 * 	SELECT MAX(AÑOFOLIO)FROM TCABECERAFOLIO;
		SELECT MAX(NROFOLIO)FROM TCABECERAFOLIO WHERE AÑOFOLIO='2021';
	 * */
	@Query(value="SELECT MAX(AÑOFOLIO) FROM TCABECERAFOLIO", nativeQuery= true)
	Integer obtenerMaxAño();
	
	@Query(value="SELECT MAX(NROFOLIO) FROM TCABECERAFOLIO WHERE AÑOFOLIO=:año", nativeQuery=true)
	Integer obtenerMaxFolioWhereAño(@Param("año") Integer año);
	
	@Query(value="select folio.nextval from dual", nativeQuery= true)
	Integer obtenerFolioNext();
	
	@Query(value="select folio.currval from dual", nativeQuery= true)
	Integer obtenerFolioCurrval();
	
	/*
	 	-- LINEA PARA REINICIAR LA SECUENCIA, SERIA EL VALOR ACTUAL MENOS 1, ES DECIR QUE SI LA SEQUENCIA VA EN 21, SERIA -20
		ALTER SEQUENCE PRUEBAA INCREMENT by -20; 
		
		SELECT PRUEBAA.NEXTVAL FROM DUAL;
		-- REESTABLECER EL VALOR INCREMENTAL A 1
		ALTER SEQUENCE PRUEBAA INCREMENT by 1;  
	 * */
	
	@Query(value="ALTER SEQUENCE folio INCREMENT BY :valor",nativeQuery=true)
	void alterSequence(@Param("valor") Integer valor);
	
	@Query(value="SELECT folio.nextval FROM DUAL",nativeQuery=true)
	void alterSequence2();
	
	@Query(value="ALTER SEQUENCE folio INCREMENT by 1",nativeQuery=true)
	void reestablecerSequence();
}
