package com.example.springsocial.repository;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.springsocial.model.CabeceraFolioModelN;
import com.example.springsocial.model.DetalleFolioModelN;


@Repository
@Transactional
public interface CabeceraFolioRepositoryN extends 
CrudRepository<CabeceraFolioModelN, Long>,
JpaRepository<CabeceraFolioModelN, Long>
{
	@Query(value="SELECT MAX(ID) FROM TCABECERAFOLIO_N", nativeQuery= true)
	Long selectMaxID();
	
	@Query(value="SELECT MAX(IDPAQUETE) FROM TCABECERAFOLIO_N WHERE ORIGEN = 2", nativeQuery= true)
	Long selectMaxFolio();
	
	// DEVUELVE LOS FOLIOS Y LINEAS DE CADA FOLIO
	@Query(value=	"SELECT DISTINCT * FROM "
				+ 	"TCABECERAFOLIO_N WHERE TO_DATE(FECCRE)=TO_DATE(:fecha,'DD/MM/YYYY')", nativeQuery=true)
	List<CabeceraFolioModelN> selectFolioAndLineas(@Param("fecha") String fecha);
	
}
