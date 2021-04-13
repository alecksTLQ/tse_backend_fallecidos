package com.example.springsocial.crud;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.generic.StringTools;
import com.example.springsocial.repository.ElementRepository;
import com.example.springsocial.repository.EntitiRepository;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings({"rawtypes","unchecked"})
public class CRUD {
	private ModelValidation modelValidation =  new ModelValidation();
	private ModelSetGet modelSetGet =  new ModelSetGet();
	private RepositorySetGet repositorySetGet= new RepositorySetGet();
	private ObjectSetGet objectSetGet =  new ObjectSetGet();
	private DateTools dateTools = new DateTools();
	private StringTools stringTools = new StringTools();
	private RestResponse response;
	/*Required*/
	private UserPrincipal userPrincipal;
	private HttpServletRequest request;
	private String modelName;
	private Object repository;
	private EntitiRepository entitiRepository;
	private ElementRepository elementRepository;
	private Object model; 
	private String modelPathFolder = "com.example.springsocial.model";
	private ObjectMapper mapper = new ObjectMapper();
	
	static final String updatedAt_text="updatedAt";
	static final String createdAt_text="createdAt";
	static final String updatedBy_text="updatedBy";
	static final String createdBy_text="createdBy";
	static final String isActive_text="isActive";
	static final String company_id_text="companyId";
	
	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		try {
			stringTools.setValue(this.modelName);
			stringTools.capitalize();
			Class<?> createElement= (Class<?>) Class.forName(this.modelPathFolder+"."+stringTools.getValue());
			this.model = mapper.convertValue(model,   createElement);;	
		}catch(Exception exception) {
			this.model=null;
			response.setError(new CustomException("Error al asignar los datos",exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName()));
		}
	}

	public void checkIfRouteHasAccess() {
		response= new RestResponse();
		if (this.request!=null)
			if (!this.userPrincipal.hasPermissionToRoute(this.request))
				response.setError(new CustomException("Acceso no autorizado",ErrorCode.ACCESS_DENIED, this.getClass().getSimpleName(),0));
	}
	
	public void view() {
		response= new RestResponse();
		checkIfRouteHasAccess();
		if (response.getError()==null)
			response.setData(true);
	}
	
	public void updateWithoutSecurity() {
		try {
			response= new RestResponse();
			modelValidation.setEntitiRepository(entitiRepository);
			modelValidation.setElementRepository(elementRepository);
			modelValidation.setRepository(repository);
			modelValidation.setCompanyId(userPrincipal.getCompany_id());
			modelValidation.setModel(this.model);
			modelValidation.setModelName(this.modelName);
			modelValidation.updateValidation();
			
			repositorySetGet.setRepository(repository);
			modelSetGet.setModel(this.model);
			Object id =  modelSetGet.getValue("id", "ID");		
			repositorySetGet.findById(id);
			Optional findedElement= (Optional) repositorySetGet.getValue();
			if (!findedElement.isPresent())
				throw new Exception("Datos no encontrados");
			
			objectSetGet.setObject(findedElement.get());
			
			if (objectSetGet.existAttribute("isActive")) 
				if (!(Boolean)objectSetGet.getValue("isActive")) 
					throw new Exception("No puede modificar un registro deshabilitado");
			
			Long createdBy = (Long)objectSetGet.getValue("createdBy");
			Date createdAt = (Date)objectSetGet.getValue("createdAt");
			
			modelSetGet.setValue(updatedAt_text, "fecha de actualización", dateTools.get_CurrentDate());
			modelSetGet.setValue(updatedBy_text, "usuario de actualización", userPrincipal.getId());
			modelSetGet.setValue(createdBy_text, "usuario de creación", (createdBy == null)  ? this.userPrincipal.getId(): createdBy);
			modelSetGet.setValue(createdAt_text, "fecha de creación", (createdAt == null)  ? dateTools.get_CurrentDate() : createdAt);
			
			objectSetGet.clearExceptValues();
			objectSetGet.addExceptValues("id");
			objectSetGet.addExceptValues("companyId");
			
			for(Field param: getAllFields(new LinkedList<Field>(), this.model.getClass())){
				Object paramValue=modelSetGet.getValue(param.getName().toString(),"");
				objectSetGet.setValue(param.getName().toString(),paramValue);
			}
			
			repositorySetGet.save(objectSetGet.getObject());
			response.setData(repositorySetGet.getValue());
		}catch(CustomException exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.getByCode(exception.getCode()),this.getClass().getSimpleName(),exception.getMessageList());
			response.setError(ex);
    	}catch(Exception exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(ex);
    	}
	}
	
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));

	    if (type.getSuperclass() != null) {
	        getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}

	public void update() {
		try {
			response= new RestResponse();
			checkIfRouteHasAccess();
			if (response.getError()==null) {
				updateWithoutSecurity();
			}
    	}catch(Exception exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(ex);
    	}
	}
	
	public void enable(Long id) {
		try {
			response= new RestResponse();
			checkIfRouteHasAccess();
			if (response.getError()==null) {
				repositorySetGet.setRepository(repository);
				
				repositorySetGet.findById(id);
				Optional element= (Optional) repositorySetGet.getValue();
				if (!element.isPresent()) 
					throw new Exception("Registro no existe");
				
				this.model=element.get();
				modelSetGet.setModel(this.model);
				
				Boolean isActive = (Boolean) modelSetGet.getValue("isActive", "habilitado");
				if (isActive==null) isActive=false;
	    		if (isActive)
	    			throw new Exception("Registro esta habilitado");
	    		
	    		modelSetGet.setValue(updatedAt_text, "fecha de actualización", dateTools.get_CurrentDate());
	    		modelSetGet.setValue(updatedBy_text, "usuario de actualización", userPrincipal.getId());
	    		modelSetGet.setValue(isActive_text, "habilitado", true);
	    		
	    		repositorySetGet.save(this.model);
	    		response.setData(repositorySetGet.getValue());
			}
    	}catch(CustomException exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.getByCode(exception.getCode()),this.getClass().getSimpleName(),exception.getMessageList());
			response.setError(ex);
    	}catch(Exception exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(ex);
    	}
	}
	
	public void disableWithoutSecurity(Long id) {
		try {
			response= new RestResponse();
			 
			repositorySetGet.setRepository(repository);
			repositorySetGet.findById(id);
			Optional element= (Optional) repositorySetGet.getValue();
			if (!element.isPresent()) 
				throw new Exception("Registro no existe");
			
			this.model=element.get();
			modelSetGet.setModel(this.model);
			
			Boolean isActive = (Boolean) modelSetGet.getValue("isActive", "habilitado");
    		if (!isActive)
    			throw new Exception("Registro esta deshabilitado");
    		
    		modelSetGet.setValue(updatedAt_text, "fecha de actualización", dateTools.get_CurrentDate());
    		modelSetGet.setValue(updatedBy_text, "usuario de actualización", userPrincipal.getId());
    		modelSetGet.setValue(isActive_text, "habilitado", false);
    		
    		repositorySetGet.save(this.model);
    		response.setData(repositorySetGet.getValue());
		 
    	}catch(CustomException exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.getByCode(exception.getCode()),this.getClass().getSimpleName(),exception.getMessageList());
			response.setError(ex);
    	}catch(Exception exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(ex);
    	}
	}
	
	public void disable(Long id) {
		try {
			response= new RestResponse();
			checkIfRouteHasAccess();
			if (response.getError()==null) {
				disableWithoutSecurity(id);
			}
    	}catch(Exception exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(ex);
    	}
	}
	
		
	public void create() {
		try {
			response= new RestResponse();
			checkIfRouteHasAccess();
			if (response.getError()==null) {
	    		modelSetGet.setModel(this.model);
	    		modelSetGet.setValue(createdAt_text, "fecha de creación", dateTools.get_CurrentDate());
	    		modelSetGet.setValue(createdBy_text, "usuario de creación", userPrincipal.getId());
	    		modelSetGet.setValue(company_id_text, "codigo de compañia", userPrincipal.getCompany_id());
	    		
	    		modelValidation.setEntitiRepository(entitiRepository);
	    		modelValidation.setElementRepository(elementRepository);
	    		modelValidation.setRepository(repository);
	    		modelValidation.setCompanyId(userPrincipal.getCompany_id());
	    		modelValidation.setModel(this.model);
	    		modelValidation.setModelName(this.modelName);
	    		modelValidation.createValidation();
	    		
	    		repositorySetGet.setRepository(repository);
	    		repositorySetGet.save(this.model);
	    		response.setData(repositorySetGet.getValue());
			}
    	}catch(CustomException exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.getByCode(exception.getCode()),this.getClass().getSimpleName(),exception.getMessageList());
			response.setError(ex);
    	}catch(Exception exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(ex);
    	}
	}
	
	public void list(Optional<String> searchCriteria, Optional<String> orderCriteria) {
		try {
			response= new RestResponse();
			checkIfRouteHasAccess();
			if (response.getError()==null) {
				repositorySetGet.setRepository(repository);
				repositorySetGet.findAll(searchCriteria, orderCriteria,userPrincipal.getCompany_id());
				this.response.setData(repositorySetGet.getValue());
			}
		}catch(CustomException exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.getByCode(exception.getCode()),this.getClass().getSimpleName(),exception.getMessageList());
			response.setError(ex);
    	}catch(Exception exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(ex);
    	}
	}
	
	public void page(Optional<String> searchCriteria, Optional<String> orderCriteria,Integer pageNumber, Integer pageSize) {
		
		try {
			response= new RestResponse();
			checkIfRouteHasAccess();
			if (response.getError()==null) {
				repositorySetGet.setRepository(repository);
				repositorySetGet.page(searchCriteria, orderCriteria,pageNumber, pageSize,userPrincipal.getCompany_id());
				this.response.setData(repositorySetGet.getValue());
			}
		}catch(CustomException exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.getByCode(exception.getCode()),this.getClass().getSimpleName(),exception.getMessageList());
			response.setError(ex);
    	}catch(Exception exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(ex);
    	}
	}
	
	public void custom(String method, Object param) {
		try {
			response= new RestResponse();
			checkIfRouteHasAccess();
			if (response.getError()==null) {
				repositorySetGet.setRepository(repository);
				repositorySetGet.custom(method,param);
				this.response.setData(repositorySetGet.getValue());
			}
		}catch(CustomException exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.getByCode(exception.getCode()),this.getClass().getSimpleName(),exception.getMessageList());
			response.setError(ex);
    	}catch(Exception exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(ex);
    	}
	}
	
	public void delete(Long id) {
		try {
			response= new RestResponse();
			checkIfRouteHasAccess();
			if (response.getError()==null) {
				repositorySetGet.setRepository(repository);
				repositorySetGet.findById(id);
				Optional element = (Optional) repositorySetGet.getValue();
				repositorySetGet.delete(element.get());
				this.response.setData(repositorySetGet.getValue());	
			}
		}catch(CustomException exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.getByCode(exception.getCode()),this.getClass().getSimpleName(),exception.getMessageList());
			response.setError(ex);
    	}catch(Exception exception) {
    		CustomException ex=  new CustomException(exception.getMessage(),exception,ErrorCode.REST_UPDATE,this.getClass().getSimpleName());
			response.setError(ex);
    	}
	}

	public RestResponse getResponse() {
		return response;
	}

	public void setResponse(RestResponse response) {
		this.response = response;
	}

	public UserPrincipal getUserPrincipal() {
		return userPrincipal;
	}

	public void setUserPrincipal(UserPrincipal userPrincipal) {
		this.userPrincipal = userPrincipal;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Object getRepository() {
		return repository;
	}

	public void setRepository(Object repository) {
		this.repository = repository;
	}
	
	public EntitiRepository getEntitiRepository() {
		return entitiRepository;
	}

	public void setEntitiRepository(EntitiRepository entitiRepository) {
		this.entitiRepository = entitiRepository;
	}

	public ElementRepository getElementRepository() {
		return elementRepository;
	}

	public void setElementRepository(ElementRepository elementRepository) {
		this.elementRepository = elementRepository;
	}
	
	public String getModelPathFolder() {
		return modelPathFolder;
	}

	public void setModelPathFolder(String modelPathFolder) {
		this.modelPathFolder = modelPathFolder;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
