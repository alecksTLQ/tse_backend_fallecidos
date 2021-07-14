package com.example.springsocial.repository;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.springsocial.model.DetalleFolioHistoricoModelN;
import com.example.springsocial.model.DetalleFolioModelN;


@Repository
@Transactional
public interface DetalleFolioHistoricoRepository extends
CrudRepository<DetalleFolioHistoricoModelN, Long>,
JpaRepository<DetalleFolioHistoricoModelN, Long>
{
	
	//CONSULTA POR RANGO DE FECHAS
	@Query(value="SELECT * FROM TDETALLEFOLIO_N td INNER JOIN TCABECERAFOLIO_N tc ON td.idcabecera=tc.idpaquete AND TO_DATE(td.feccre) BETWEEN TO_DATE(:fechainicio,'DD/MM/YYYY') AND TO_DATE(:fechafinal,'DD/MM/YYYY')",nativeQuery=true)
	List<DetalleFolioModelN> selectByRangoFechas(@Param("fechainicio") String fechainicio, @Param("fechafinal") String fechafinal);
	
	@Query(value="SELECT * FROM TDETALLEFOLIO_N td INNER JOIN TCABECERAFOLIO_N tc ON td.idcabecera=tc.idpaquete   AND TO_DATE(td.feccre) "
			+ "BETWEEN TO_DATE(:fechainicio,'DD/MM/YYYY') AND TO_DATE(:fechafinal,'DD/MM/YYYY')",nativeQuery=true)
	List<DetalleFolioModelN> selectByRangoFechasHistorico(@Param("fechainicio") String fechainicio, @Param("fechafinal") String fechafinal);
}