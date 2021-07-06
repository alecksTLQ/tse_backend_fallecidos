package com.example.springsocial.repository;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springsocial.model.DetalleFolioModelN;



@Repository
@Transactional
public interface DetalleFolioRepositoryN extends
CrudRepository<DetalleFolioModelN, Long>,
JpaRepository<DetalleFolioModelN, Long>
{
	@Query(value="SELECT MAX(NROLINEA) FROM TDETALLEFOLIO_N WHERE IDPAQUETE =:id AND AÑOFOLIO=:año", nativeQuery= true)
	Integer selectMaxLinea(@Param("id") String di, @Param("año") Integer año);
	
	//List<DetalleFolioModelN> findByIDPAQUETEAndAÑOFOLIO(String IDPAQUETE, Integer AÑOFOLIO);
		
	DetalleFolioModelN findByID(Integer ID);
	//FECHA DE CREACION
	@Query(value="SELECT * FROM TDETALLEFOLIO_N INNER JOIN TCABECERAFOLIO_N ON TDETALLEFOLIO_N.IDCABECERA=TCABECERAFOLIO_N.ID WHERE TCABECERAFOLIO_N.ORIGEN=:origen AND TO_DATE(TDETALLEFOLIO_N.FECCRE)=TO_DATE(:fecha,'DD/MM/YYYY')", nativeQuery= true)
	List<DetalleFolioModelN> selectByFechaCreacion(@Param("fecha") String fechacreacion, @Param("origen") Integer origen);
	
	
	//VERIFICADOS Y POR USUARIO
	@Query(value="SELECT * FROM TDETALLEFOLI_ON "
			+ "INNER JOIN TCABECERAFOLIO_N ON "
			+ "TDETALLEFOLIO_N.IDPAQUETE=TCABECERAFOLIO_N.IDPAQUETE AND "
			+ "TDETALLEFOLIO_N.AÑOFOLIO=TCABECERAFOLIO_N.AÑO AND "
			+ "TCABECERAFOLIO_N.VERIFICA='V' AND TDETALLEFOLIO_N.USRCRE=:usuario", nativeQuery= true)
	
	List<DetalleFolioModelN> selectVerificadoAndUsuario(@Param("usuario") String usuario);
	
	
	// DEVUELVE LOS IDPAQUETES Y LAS LINEAS UTILIZADAS EN DICHO PAQUETE DE ACUERDO A LA FECHA DE CREACION
	@Query(value=	"SELECT DISTINCT * FROM "
				+ 	"TDETALLEFOLION INNER JOIN TCABECERAFOLIO_N ON TDETALLEFOLIO_N.IDPAQUETE=TCABECERAFOLIO_N.IDPAQUETE "
				+ 	"AND TDETALLEFOLIO_N.AÑOFOLIO=TCABECERAFOLIO_N.AÑO "
				+ 	"AND TO_DATE(TDETALLEFOLIO_N.FECCRE)=TO_DATE(:fecha,'DD/MM/YYYY')", nativeQuery=true)
	List<DetalleFolioModelN> selectFolioAndLineas(@Param("fecha") String fecha);
	
	//CUENTA LA CANTIDAD DE LINEAS NO VERIFICADAS POR IDPAUQETE Y AÑO
	@Query(value="SELECT COUNT(*) AS TOTAL FROM TDETALLEFOLIO_N WHERE USRVERIFI='N' AND AÑOFOLIO=:año AND IDPAQUETE=:idpaquete", nativeQuery=true)
	Integer obtenerNoVerificado(@Param("idpaquete") String idpaquete, @Param("año") Integer año);
	
	
}