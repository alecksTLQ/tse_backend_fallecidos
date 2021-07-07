package com.example.springsocial.repository;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.springsocial.model.DetalleFolioHistoricoModelN;


@Repository
@Transactional
public interface DetalleFolioHistoricoRepository extends
CrudRepository<DetalleFolioHistoricoModelN, Long>,
JpaRepository<DetalleFolioHistoricoModelN, Long>
{
	
}
