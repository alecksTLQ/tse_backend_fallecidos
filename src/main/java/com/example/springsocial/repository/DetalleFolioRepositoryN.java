package com.example.springsocial.repository;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springsocial.model.DetalleFolioModelN;
import com.example.springsocial.model.IdpaqueteDetalle;



@Repository
@Transactional
public interface DetalleFolioRepositoryN extends
CrudRepository<DetalleFolioModelN, Integer>,
JpaRepository<DetalleFolioModelN, Integer>
{
	@Query(value="SELECT MAX(NROLINEA) FROM TDETALLEFOLION WHERE IDPAQUETE =:id AND AÑOFOLIO=:año", nativeQuery= true)
	Integer selectMaxLinea(@Param("id") String di, @Param("año") Integer año);
	
	List<DetalleFolioModelN> findByIDPAQUETEAndAÑOFOLIO(String IDPAQUETE, Integer AÑOFOLIO);
}