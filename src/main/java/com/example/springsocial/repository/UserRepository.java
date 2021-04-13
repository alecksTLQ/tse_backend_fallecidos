package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.springsocial.generic.CrudCustom;
import com.example.springsocial.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>, 
										PagingAndSortingRepository<User, Integer>, 
										JpaSpecificationExecutor<User>, 
										JpaRepository<User, Integer>, 
										CrudCustom<User> {
	List<User> findAllByUserId(Long userId);
	User findByUserId(Long userId);
}
