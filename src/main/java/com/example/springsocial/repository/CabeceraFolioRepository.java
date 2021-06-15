package com.example.springsocial.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springsocial.model.CabeceraFolioModell;
import com.example.springsocial.model.IdCabecera;

@Repository
@Transactional
public interface CabeceraFolioRepository extends 
CrudRepository<CabeceraFolioModell, Integer>,
JpaRepository<CabeceraFolioModell, Integer>
{
	@Query(value="select nrofolio.nextval from dual", nativeQuery= true)
	Integer selectNoFolio();
}

