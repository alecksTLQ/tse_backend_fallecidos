package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.model.NoFolioModel;

@Repository
@Transactional
public interface NoFolioRepository extends 
CrudRepository<NoFolioModel, Integer>,
JpaRepository<NoFolioModel, Integer>
{
	
	@Query(value = "SELECT MAX(NROFOLIO) AS FOLIO FROM TNROFOLIO WHERE año = :valor", nativeQuery=true)
	Integer findByaño(@Param("valor") Integer valor);
	
	/* OBTENCION DE NROFOLIO MINIMO DONDE USUARIO, AÑO = ACTUAL Y ESTADO = 1 */
	@Query(value = "SELECT MIN(nrofolio) FROM TNROFOLIO WHERE USUARIO = :usuario AND AÑO = :año AND ESTADO = '1'", nativeQuery=true)
	Integer SelectNoFolio(@Param("usuario") String usuario, @Param("año") Integer año);
	
	/* OBTENCION DE NROLINEA DONDE NROFOLIO=n Y AÑO = ACTUAL */
	@Query(value = "SELECT NROLINEA FROM TNROFOLIO WHERE NROFOLIO =:folio AND AÑO =:año", nativeQuery=true)
	Integer SelectNoLinea(@Param("folio") Integer folio, @Param("año") Integer año);
	
	/* OBTENCION DE REGISTROS DE NO FOLIOS DONDE AÑO = ACTUAL */
	@Query(value="SELECT * FROM TNROFOLIO WHERE AÑO= :año ORDER BY NROFOLIO ASC", nativeQuery=true)
	List<NoFolioModel> findByAllAño(@Param("año") Integer año);
	
	@Query(value="SELECT * FROM TNROFOLIO WHERE NROFOLIO =:nofolio", nativeQuery=true)
	NoFolioModel findByFolio(@Param("nofolio") Integer nofolio);
	
	@Query(value="DELETE FROM TNROFOLIO WHERE USUARIO =:usuario AND NROFOLIO =:nofolio AND AÑO =:año AND ESTADO='1' AND NROLINEA='0'", nativeQuery=true)
	void DeleteByNofolio(@Param("usuario") String usuario, @Param("nofolio") Integer nofolio, @Param("año") Integer año);
	
	@Query(value="select folio.nextval from dual", nativeQuery= true)
	Integer selectNoFolio();
	
}