package com.example.springsocial.generic;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.crud.CRUD;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.RestResponse;

@SuppressWarnings({"rawtypes"})
@RestController
public interface CrudController {
	CRUD crud = new CRUD();
	Map<String, Object> repositories = new HashMap<>();

	@GetMapping("view")
    @PreAuthorize("hasRole('USER')")
    public default RestResponse view(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request) {
		crud.setModelName(getModelNameFromPath(request));
		crud.setRepository(getRepositoryByPath(crud.getModelName()));
		crud.setRequest(request);
		crud.setUserPrincipal(userPrincipal);
		crud.view();
    	return crud.getResponse();
    }
	
	@GetMapping("list")
    @PreAuthorize("hasRole('USER')")
    public default RestResponse list(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestParam("searchCriteria") Optional<String> searchCriteria,@RequestParam Optional<String> orderCriteria) {
		crud.setModelName(getModelNameFromPath(request));
		crud.setRepository(getRepositoryByPath(crud.getModelName()));
		crud.setRequest(request);
		crud.setUserPrincipal(userPrincipal);
    	crud.list(searchCriteria, orderCriteria);
    	return crud.getResponse();
    }
	
	@GetMapping("list/{pageNumber}/{pageSize}")
    @PreAuthorize("hasRole('USER')")
    public default RestResponse page(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request, @RequestParam("searchCriteria") Optional<String> searchCriteria,@RequestParam Optional<String> orderCriteria,@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
		crud.setModelName(getModelNameFromPath(request));
		crud.setRepository(getRepositoryByPath(crud.getModelName()));
		crud.setRequest(request);
		crud.setUserPrincipal(userPrincipal);
    	crud.page(searchCriteria, orderCriteria, pageNumber,  pageSize);
    	return crud.getResponse();
    }
	
	@PutMapping("update")
	public default RestResponse update(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@RequestBody Object updateElement) {
		crud.setModelName(getModelNameFromPath(request));
		crud.setRepository(getRepositoryByPath(crud.getModelName()));
		crud.setRequest(request);
		crud.setUserPrincipal(userPrincipal);
		crud.setModel(updateElement);
		crud.update();
		return crud.getResponse();		
	}
	
	@PostMapping("create")
    @PreAuthorize("hasRole('USER')")
    public default RestResponse create(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@RequestBody Object  createElement) {
		crud.setModelName(getModelNameFromPath(request));
		crud.setRepository(getRepositoryByPath(crud.getModelName()));
		crud.setRequest(request);
		crud.setUserPrincipal(userPrincipal);
    	crud.setModel(createElement);
    	crud.create();
		return crud.getResponse();
    }
	
	@PatchMapping("enable/{id}")
    @PreAuthorize("hasRole('USER')")
    public default RestResponse enable(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@PathVariable Long id) {
		crud.setModelName(getModelNameFromPath(request));
		crud.setRepository(getRepositoryByPath(crud.getModelName()));
		crud.setRequest(request);
		crud.setUserPrincipal(userPrincipal);
    	crud.enable(id);
    	return crud.getResponse();
    }
	
	@PatchMapping("disable/{id}")
    @PreAuthorize("hasRole('USER')")
    public default RestResponse disable(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@PathVariable Long id) {
		crud.setModelName(getModelNameFromPath(request));
		crud.setRepository(getRepositoryByPath(crud.getModelName()));
		crud.setRequest(request);
		crud.setUserPrincipal(userPrincipal);
    	crud.disable(id);
    	return crud.getResponse();
    }
	
	@DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public default RestResponse delete(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request,@PathVariable Long id) {
		crud.setModelName(getModelNameFromPath(request));
		crud.setRepository(getRepositoryByPath(crud.getModelName()));
		crud.setRequest(request);
		crud.setUserPrincipal(userPrincipal);
    	crud.delete(id);
    	return crud.getResponse();
    }
	
	default String getModelNameFromPath(HttpServletRequest request) {
		String path = request.getRequestURI();
    	String 	[]parts =  path.split("/");
    	return parts[1];
	}
	
	default Object getRepositoryByPath(String path) {
		return repositories.get(path);
	}
}

