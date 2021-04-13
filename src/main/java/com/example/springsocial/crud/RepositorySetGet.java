package com.example.springsocial.crud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.generic.GenericClass;

@SuppressWarnings({"unchecked","rawtypes"})
public class RepositorySetGet {
	private Object repository;
	private GenericClass genericClass;
	private Object value;
	
	public Object getRepository() {
		return repository;
	}

	public void setRepository(Object repository) {
		this.repository = repository;
	}
	
	public void executeMethod(String method, Object param, String errorMessage) throws CustomException {
		genericClass= new GenericClass(this.repository,method,param);
		genericClass.executeMethod();
		if (genericClass.getIsError()==true) 
			throw new CustomException(errorMessage,ErrorCode.GENERIC_ERROR, this.getClass().getSimpleName().toString(),  Thread.currentThread().getStackTrace()[0].getLineNumber());
		
		this.setValue(genericClass.getResult());
	}
	
	public Object getValue() {
		return this.value;
	}
		
	
	public void findAllById(Collection<Long> ids ) throws CustomException {
		executeMethod("findAllById",ids,"Error al buscar el ID");
	}
	
	public void findAllById(Long id ) throws CustomException {
		Collection<Long> ids = new ArrayList<>();
		ids.add(id);
		executeMethod("findAllById",ids,"Error al buscar el ID");
	}
	
	public void custom(String method, Object param ) throws CustomException {
		executeMethod(method,param,"Error al buscar los datos");
	}
	
	public void findById(Object param) throws CustomException {
		executeMethod("findById",param,"Error al buscar el ID");
	}
	
	public void findAll(Optional<String> searchCriteria, Optional<String> orderCriteria, Long companyId) throws CustomException {
		executeMethod("findAll",new Object []{searchCriteria,orderCriteria,companyId},"Error al listar");
	}
	
	public void findAll(Optional<String> searchCriteria, Optional<String> orderCriteria) throws CustomException {
		executeMethod("findAll",new Object []{searchCriteria,orderCriteria},"Error al listar");
	}
	
	public void page(Optional<String> searchCriteria, Optional<String> orderCriteria, Integer pageNumber, Integer pageSize,Long companyId) throws CustomException {
		executeMethod("page",new Object []{searchCriteria,orderCriteria,pageNumber,pageSize,companyId},"Error al listar");
	}
	
	public void page(Optional<String> searchCriteria, Optional<String> orderCriteria, Integer pageNumber, Integer pageSize) throws CustomException {
		executeMethod("page",new Object []{searchCriteria,orderCriteria,pageNumber,pageSize},"Error al listar");
	}
	
	public void save(Object element) throws CustomException {
		executeMethod("save",element,"Error al guardar");
	}
	
	public void update(Object element) throws CustomException {
		executeMethod("update",element,"Error al modificar");
	}
	
	public void delete(Object element) throws CustomException {
		executeMethod("delete",element,"Error al eliminar");
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
