package com.example.springsocial.process;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.springsocial.crud.CRUD;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.generic.CrudController;
import com.example.springsocial.model.CabeceraFolioModell;
import com.example.springsocial.model.NoFolioModel;
import com.example.springsocial.repository.CabeceraFolioRepository;
import com.example.springsocial.repository.ElementRepository;
import com.example.springsocial.repository.EntitiRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;
import com.example.springsocial.transaction.CabeceraFolioTransaction;


@SuppressWarnings({"rawtypes", "unchecked","unused"})
@Service
public class CabeceraFolioProcess <T> implements CrudController {

	
	/*public RestResponse createe(CabeceraFolioModell mdlfolio) {
		
		try {
			
			cabeceraTransaction.setEntityManagerFactory(entityManagerFactory);
			cabeceraTransaction.save(mdlfolio);
			
				if(cabeceraTransaction.GetResponse().getError()!=null) throw new Exception(cabeceraTransaction.GetResponse().getError().toString());
				else {
					response.setData("Insercion cabecera folio "+cabeceraTransaction.GetResponse().getData());
				}
			
			
		}catch(Exception e) {
			System.out.print(e.getMessage());
			response.setData(e.getMessage());
		}
		
		return response;
	}*/
}
