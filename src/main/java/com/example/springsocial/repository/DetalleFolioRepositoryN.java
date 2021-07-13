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
	
	DetalleFolioModelN findByID(Integer ID);
	//FECHA DE CREACION, ORIGEN Y USUARIO
	@Query(value="SELECT * FROM TDETALLEFOLIO_N INNER JOIN TCABECERAFOLIO_N ON TDETALLEFOLIO_N.IDCABECERA=TCABECERAFOLIO_N.ID WHERE TCABECERAFOLIO_N.ORIGEN=:origen "
			+ "AND TDETALLEFOLIO_N.USRCRE=:user  AND TO_DATE(TDETALLEFOLIO_N.FECCRE)=TO_DATE(:fecha,'DD/MM/YYYY')", nativeQuery= true)
	List<DetalleFolioModelN> selectByFechaCreacion(@Param("fecha") String fechacreacion, @Param("origen") Integer origen, @Param("user") String user);
	
	//BUSQUEDA POR FOLIO AÑO Y ORIGEN
	@Query(value="SELECT * FROM TDETALLEFOLIO_N td INNER JOIN TCABECERAFOLIO_N tc ON tc.id =td.idcabecera "
			+ "AND tc.AÑO=:año AND tc.ORIGEN=:origen AND tc.IDPAQUETE=:folio", nativeQuery= true)
	List<DetalleFolioModelN> selectByFolioAndOrigen(@Param("folio") Integer folio, @Param("año") Integer año, @Param("origen") Integer origen );
	
	//BUSQUEDA POR CEDULA
	@Query(value="SELECT * FROM TDETALLEFOLIO_N td INNER JOIN TCABECERAFOLIO_N tc ON tc.id =td.idcabecera "
			+ "AND td.nroorden=:orden AND td.nroregist=:registro", nativeQuery=true)
	List<DetalleFolioModelN> selectByCedula(@Param("orden") String orden, @Param("registro") Integer registro);
	
	//
	@Query(value="SELECT * FROM TDETALLEFOLIO_N td INNER JOIN TCABECERAFOLIO_N tc ON tc.id =td.idcabecera "
			+ "and soundex(td.NOM1FALLE)=soundex(:nom1) and soundex(td.NOM2FALLE)=soundex(:nom2) and soundex(td.APE1FALLE)=soundex(:ape1) and soundex(td.APE2FALLE)=soundex(:ape2) and soundex(td.APE3FALLE)=soundex(:ape3)",nativeQuery=true)
	List<DetalleFolioModelN> selectByNombre5(@Param("nom1") String nom1, @Param("nom2") String nom2, @Param("ape1") String ape1, @Param("ape2") String ape2, @Param("ape3") String ape3);

	@Query(value="SELECT * FROM TDETALLEFOLIO_N td INNER JOIN TCABECERAFOLIO_N tc ON tc.id =td.idcabecera "
			+ "and soundex(td.NOM1FALLE)=soundex(:nom1) and soundex(td.APE1FALLE)=soundex(:ape1) and soundex(td.APE2FALLE)=soundex(:ape2)",nativeQuery=true)
	List<DetalleFolioModelN> selectByNombre3(@Param("nom1") String nom1,@Param("ape1") String ape1, @Param("ape2") String ape2);
	
	@Query(value="SELECT * FROM TDETALLEFOLIO_N td INNER JOIN TCABECERAFOLIO_N tc ON tc.id =td.idcabecera "
			+ "and soundex(td.NOM1FALLE)=soundex(:nom1) and soundex(td.APE1FALLE)=soundex(:ape1)",nativeQuery=true)
	List<DetalleFolioModelN> selectByNombre2(@Param("nom1") String nom1, @Param("ape1") String ape1);
	
	@Query(value="SELECT * FROM TDETALLEFOLIO_N td INNER JOIN TCABECERAFOLIO_N tc ON tc.id =td.idcabecera "
			+ "and soundex(td.NOM1FALLE)=soundex(:nom1) and soundex(td.NOM2FALLE)=soundex(:nom2) and soundex(td.APE1FALLE)=soundex(:ape1) and soundex(td.APE2FALLE)=soundex(:ape2)",nativeQuery=true)
	List<DetalleFolioModelN> selectByNombre4(@Param("nom1") String nom1, @Param("nom2") String nom2, @Param("ape1") String ape1, @Param("ape2") String ape2);

	@Query(value="SELECT * FROM TDETALLEFOLIO_N td INNER JOIN TCABECERAFOLIO_N tc ON tc.id =td.idcabecera "
			+ "and soundex(td.NOM1FALLE)=soundex(:nom1) and soundex(td.NOM2FALLE)=soundex(:nom2) and soundex(td.APE1FALLE)=soundex(:ape1)",nativeQuery=true)
	List<DetalleFolioModelN> selectByNombre6(@Param("nom1") String nom1,@Param("nom2") String nom2, @Param("ape1") String ape1);
	
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
	
	
	
	//CONSULTA POR RANGO DE FECHAS
	@Query(value="SELECT * FROM TDETALLEFOLIO_N td INNER JOIN TCABECERAFOLIO_N tc ON td.idcabecera=tc.idpaquete AND TO_DATE(td.feccre) BETWEEN TO_DATE(:fechainicio,'DD/MM/YYYY') AND TO_DATE(:fechafinal,'DD/MM/YYYY')",nativeQuery=true)
	List<DetalleFolioModelN> selectByRangoFechas(@Param("fechainicio") String fechainicio, @Param("fechafinal") String fechafinal);
}