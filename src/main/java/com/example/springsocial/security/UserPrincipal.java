package com.example.springsocial.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.example.springsocial.api.Api;
import com.example.springsocial.api.ApiHeader;
import com.example.springsocial.modelSSO.User;
import com.example.springsocial.tools.RestResponse;

@SuppressWarnings({"serial","rawtypes","unused"})
public class UserPrincipal implements OAuth2User, UserDetails {
    private Long id;
    private String code,email, password, name;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
	private Optional<String> searchCriteria;
    private Optional<String> orderCriteria;
    private Long rol_id;
    private Long company_id;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Api api = new Api();
    private ApiHeader apiHeader =  new ApiHeader();
    
    public UserPrincipal(Long id, String code, String name, String email, String password, Collection<? extends GrantedAuthority> authorities, Long rol_id, Long company_id) {
        this.id = id;
        this.code=code;
        this.name=name;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.rol_id= rol_id;
        this.company_id= company_id;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user.getId(),
                user.getCode(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getRol_id(),
                user.getCompanyId()
        );
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }
    
    public boolean hasPermissionToRoute(HttpServletRequest request) {
    	try {
    		String path = request.getRequestURI();
        	String 	[]parts =  path.split("/");
        	apiHeader.setRequest(request);
        	api.setAuthorizationHeader(apiHeader.getAuthorizationHeader());
        	api.setPath("user/haspermission");
        	
        	api.addParam("form", parts[1]);
        	api.addParam("action", parts[2]);
        	
        	api.sendPost();
        	RestResponse response= api.getRestResponse();
        	if (response.getError()!=null)
        		return false;
        	if (response.getData()==null)
        		return false;
        	return (response.getData() instanceof Boolean) ? ( (Boolean)(response.getData() ) == true ? true : false ): false;
    	}catch(Exception ex) {
    		return false;
    	}
    }
    
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { return true;}
    @Override
    public boolean isEnabled() { return true;}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities;}
    @Override
    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes;}
    @Override
    public String getName() { return this.name; }
    public String getCode() { return this.code;}
    public void setCode(String code) {this.code=code;}
	public Long getRol_id() { return rol_id; }
	public void setRol_id(Long rol_id) { this.rol_id = rol_id;}
	public Long getCompany_id() { return company_id; }
	public void setCompany_id(Long company_id) { this.company_id= company_id; }
}
