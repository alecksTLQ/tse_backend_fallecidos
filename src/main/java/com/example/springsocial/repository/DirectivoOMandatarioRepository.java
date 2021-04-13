package com.example.springsocial.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.model.OrganizacionesPoliticas;

@Repository
@Transactional
public interface DirectivoOMandatarioRepository extends JpaRepository<OrganizacionesPoliticas, String> 
{   

	@Query(value = " SELECT "
    +"	dpi.cui,"
    +"	p.nroboleta,"
    +"	p.nom1||' '||p.nom2||' '||p.ape1||' '||p.ape2||' '||p.ape3 as NOMBRE,"
    +"	p.status,"    
    +"	NVL(afi.flagstatus,null) as FLAGSTATUS,"
    +"	(select count (*) AS TOTALCARGOS from"
    +"	("
    +"	SELECT"
    +"  a.nroboleta"
    +"	FROM"
    +"  tcomitedelegados A,"
    +"  TREFDEPMUN B,"
    +"  TREFDEPMUN C,"
    +"  torgpoliticas d,"
    +"  ttempopasam e,"
    +"  tasamblea f,"
    +"  tdpi"
    +"  WHERE"
    +"  A.coddep = B.coddep And B.codMun = 0 And A.coddep = C.coddep And A.codMun = C.codMun"
    +"  and a.status=0 and a.idop=d.idop"
    +"  and a.idop=e.idop and a.partida=e.partida and A.CODDEP=e.coddepcel and a.codmun=e.codmuncel"
    +"	and (e.tipoop=1 or e.tipoop=3)"
    +"  and e.idop=f.idop and e.partida=f.partida and e.folio=f.folio and e.libro=f.libro and e.tipoop=f.tipoop and e.tipoasam=f.tipoasam"
    +"  and e.CODDEPcel=f.coddepcel and e.codmuncel=f.codmuncel and f.verificado='S'"
    +"  AND ((A.CODCARGOCEM IS NOT NULL AND A.CODCARGOCEM<>0)"
    +"  or (A.CODCARGOCED IS NOT NULL AND A.CODCARGOCED<>0)"
    +"  or  (A.CODCARGOCEN IS NOT NULL AND A.CODCARGOCEN<>0))"
    +"  and A.nroboleta=tdpi.nroboleta and tdpi.cui= :cui "
    +"  )"
    +"	) as CARGO"
    +"	FROM tpadron p LEFT JOIN tafiliados afi ON p.nroboleta=afi.nroboleta,"
    +"	tdpi dpi"
    +"	WHERE"
    +"	p.nroboleta=dpi.nroboleta"
    +"	and dpi.cui= :cui"
    +"	order by nroboleta desc",nativeQuery = true)
	
	
	/*@Query(value = "select * from "
	+"	("
	+"	("
	+"	SELECT"
	+"	dpi.cui,"
	+"	p.nroboleta,"
	+"	p.nom1||' '||p.nom2||' '||p.ape1||' '||p.ape2||' '||p.ape3 as NOMBRE,"
	+"	p.status," 
	+"	NVL(afi.flagstatus, null) as FLAGSTATUS,"
	+"	(select count (*)  AS TOTALCARGOS from"
	+"	("
	+	"SELECT"
	+"	a.nroboleta"
	+" 	FROM"
    +" 	tcomitedelegados A,"
    +" 	TREFDEPMUN B, "
    +" 	TREFDEPMUN C, "
    +" 	torgpoliticas d, "
    +" 	ttempopasam e, "
    +" 	tasamblea f, "
    +" 	tdpi "
    +	"WHERE "
    +"	A.coddep = B.coddep And B.codMun = 0 And A.coddep = C.coddep And A.codMun = C.codMun"
    +" 	and a.status=0 and a.idop=d.idop "
    +" 	and a.idop=e.idop and a.partida=e.partida and A.CODDEP=e.coddepcel and a.codmun=e.codmuncel"
    +" 	and (e.tipoop=1 or e.tipoop=3) "
    +" 	and e.idop=f.idop and e.partida=f.partida and e.folio=f.folio and e.libro=f.libro and e.tipoop=f.tipoop and e.tipoasam=f.tipoasam"
    +" 	and e.CODDEPcel=f.coddepcel and e.codmuncel=f.codmuncel and f.verificado='S' "
    +" 	AND ((A.CODCARGOCEM IS NOT NULL AND A.CODCARGOCEM<>0) "
    +" 	or (A.CODCARGOCED IS NOT NULL AND A.CODCARGOCED<>0) "
    +" 	or  (A.CODCARGOCEN IS NOT NULL AND A.CODCARGOCEN<>0))"
    +" 	and A.nroboleta=tdpi.nroboleta and tdpi.cui=:cui"
    +" 	) "	
    +"	) as CARGO "
    +"	FROM tpadron p LEFT JOIN tafiliados afi ON p.nroboleta=afi.nroboleta, "
    +"	tdpi dpi, "
    +"	tafiliados a "
    +"	WHERE"
    +"	p.nroboleta=dpi.nroboleta"
    +"	and dpi.cui=:cui"
    +"	)"
    +"	UNION"
    +"	SELECT 0 as CUI, 0 as NROBOLETA, '' as NOMBRE, 0 as STATUS, 0 AS FLAGSTATUS, 0 AS TOTALCARGOS from dual "
    +"	)"
    +"	order by nroboleta desc ",nativeQuery = true)*/
	
	
	
	
	
	
	/*@Query(value ="	SELECT dpi.cui, p.nroboleta, p.nom1||' '||p.nom2||' '||p.ape1||' '||p.ape2||' '||p.ape3 as NOMBRE, p.status, "
			+"(select count (*) from(SELECT a.nroboleta FROM tcomitedelegados A, TREFDEPMUN B,TREFDEPMUN C,torgpoliticas d,ttempopasam e,"
	        +" tasamblea f,"
	        +" tdpi  WHERE "
	        +"  A.coddep = B.coddep "
	        +"  And B.codMun = 0 "
	        +"  And A.coddep = C.coddep" 
	        +"  And A.codMun = C.codMun"
	        +"  and a.status=0" 
	        +"  and a.idop=d.idop"
	        +"  and a.idop=e.idop and a.partida=e.partida and A.CODDEP=e.coddepcel and a.codmun=e.codmuncel "
	        +"  and (e.tipoop=1 or e.tipoop=3)"
	        +"  and e.idop=f.idop and e.partida=f.partida and e.folio=f.folio and e.libro=f.libro and e.tipoop=f.tipoop and e.tipoasam= f.tipoasam"
	        +"  and e.CODDEPcel=f.coddepcel and e.codmuncel=f.codmuncel and f.verificado='S' "
	        +"  AND ((A.CODCARGOCEM IS NOT NULL AND A.CODCARGOCEM<>0)"
	        +"  or (A.CODCARGOCED IS NOT NULL AND A.CODCARGOCED<>0) "
	        +"  or  (A.CODCARGOCEN IS NOT NULL AND A.CODCARGOCEN<>0))"
	        +"  and A.nroboleta=tdpi.nroboleta and tdpi.cui= :cui)"
			+"	) as CARGO "
			+"	FROM "
			+"	tpadron p, "
			+"	tdpi dpi "
			+"	WHERE "
			+"	p.nroboleta=dpi.nroboleta "
			+"	and dpi.cui= :cui ",nativeQuery = true)*/
	
	
		/*Persona listarDatos(@Param("cui") String cui);*/
	//List<OrganizacionesPoliticas> listUserData(@Param("cui") String cui);
List<String> listUserData(@Param("cui") String cui);


}
