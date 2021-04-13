package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.model.Company;
import com.example.springsocial.model.OrganizacionesPoliticas;

@Repository
@Transactional
public interface OrganizacionesPoliticasRepository extends JpaRepository<OrganizacionesPoliticas, String> 
{   

	@Query(value = "select nombreop as nombre, a.idop, siglas, d1.deslarga as departamento, d2.deslarga as municipio," 
			+"decode(o.direccion,null,'', o.direccion || decode(o.nrocasa,null,' ',o.nrocasa) || decode(o.nrozona,null,'',' ZONA ' || o.nrozona)) as direccion,"
			+"telefono, email, p.nom1 || ' ' || decode(p.nom2,null,'',p.nom2 || ' ')  ||p.ape1 || decode(p.ape2,null,'',' ' || p.ape2) || decode(trim(p.ape3),null,'',' DE ' || p.ape3) as REPRESENTANTELEGAL,"
			+"upper(r1.descripcion) as fase , upper(r2.descripcion) as estado from tpadron p, tdpi d, tafiliados a, torgpoliticas o, trefdepmun d1, trefdepmun d2, trefstatusop r1, trefstatusop r2 "
			+"where d1.coddep = o.coddep and d1.codmun = 0 "
			+"and d2.coddep = o.coddep and d2.codmun = o.codmun "
			+"and p.nroboleta = o.bolreplegal "
			+"and r1.codigo = o.faseop and r1.objeto = 'FaseOP'"
			+"and r2.codigo = o.statusop and r2.objeto = 'StatusOP'"
			+"and o.idop = a.idop "
			+"and a.nroboleta = d.nroboleta "
			+"and d.cui = :cui",nativeQuery = true)
	
	
		/*Persona listarDatos(@Param("cui") String cui);*/
	//List<OrganizacionesPoliticas> listUserData(@Param("cui") String cui);
List<String> listUserData(@Param("cui") String cui);


}