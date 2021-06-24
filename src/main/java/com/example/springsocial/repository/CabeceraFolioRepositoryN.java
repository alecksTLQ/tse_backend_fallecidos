package com.example.springsocial.repository;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.springsocial.model.CabeceraFolioModelN;


@Repository
@Transactional
public interface CabeceraFolioRepositoryN extends 
CrudRepository<CabeceraFolioModelN, String>,
JpaRepository<CabeceraFolioModelN, String>
{
	@Query(value="SELECT MAX(NROFOLIO) FROM TCABECERAFOLION WHERE USRACTUA=:usuario AND AÑOFOLIO=:año", nativeQuery= true)
	Integer selectMaxFolio(@Param("usuario") String usuario, @Param("año") Integer año);
}
