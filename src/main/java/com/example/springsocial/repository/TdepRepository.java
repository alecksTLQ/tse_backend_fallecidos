package com.example.springsocial.repository;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springsocial.model.TRefDep;
import com.example.springsocial.model.TRefMun;

@Repository
@Transactional
public interface TdepRepository 
extends
CrudRepository<TRefDep, Integer>,
JpaRepository<TRefDep, Integer>

{
	List<TRefDep> findAllByOrderByIdAsc();
}
