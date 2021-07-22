package com.example.springsocial.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springsocial.model.TRefMun;


@Repository
@Transactional
public interface TmunRepository 

extends
CrudRepository<TRefMun, Long>,
JpaRepository<TRefMun, Long>
{
	List<TRefMun> findAllByOrderByIdAsc();
}
