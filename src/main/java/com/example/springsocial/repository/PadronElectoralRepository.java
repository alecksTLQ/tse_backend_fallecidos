package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.generic.CrudCustom;
import com.example.springsocial.model.Company;
import com.example.springsocial.model.Persona;

@Repository
@Transactional
public interface PadronElectoralRepository extends CrudRepository<Company, Integer>, 
										PagingAndSortingRepository<Company, Integer>, 
										JpaSpecificationExecutor<Company>  {
		 
	
	@Query(value = "select a.nom1 || ' ' || a.nom2  nombres,  a.ape1 || ' ' || a.ape2 apellidos,  a.status as codstatus,b.descripcion,"
			+ " c.ordenced as nrocedula1,c.registroced as nrocedula2,  "
			+ "             decode(f.depvecindad,null, d.deslarga, g.DESLARGA) nomDepto,  "
			+ "             decode(f.munvecindad,null,e.deslarga, h.deslarga) nomMupio,  "
			+ "             f.cui,  "
			+ "             decode(trim(a.ape3),null,'',a.ape3) apecasada  "
			+ "             , a.nroboleta  "
			+ "             from tpadron a,trefstatuspadron b, tcedula c, trefdepmun d,trefdepmun e , tdpi f, trefdepmun g, trefdepmun h  "
			+ "             where f.cui = :cui"
			+ "             and a.status <> 2 "
			+ "             and a.status=b.codstatus   "
			+ "             and c.nroboleta(+) = a.nroboleta  "
			+ "             and d.codmun(+)=0  "
			+ "             and e.CODDEP(+) =c.depcedula  "
			+ "             and e.codmun(+) = c.muncedula  "
			+ "             and f.nroboleta = a.nroboleta  "
			+ "             and g.coddep(+) = f.depvecindad    "
			+ "             and g.codmun(+) = 0  "
			+ "             and h.coddep(+) = f.depvecindad "
			+ "             and h.codmun(+) = f.munvecindad ",nativeQuery = true)
	
	
		/*Persona listarDatos(@Param("cui") String cui);*/
	List<String> listUserData(@Param("cui") String cui);
	
	@Query(value = "select a.idop,b.siglas,b.nombreop,a.fecafiliacion from tafiliados a,torgpoliticas b"
			+ "                 where a.nroboleta=:boleta"
			+ "                 and a.idop=b.idop",nativeQuery = true)
	
	List<String> validateAfiliation(@Param("boleta") String boleta);
}



