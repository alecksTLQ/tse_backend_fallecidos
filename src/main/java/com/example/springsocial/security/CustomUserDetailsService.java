package com.example.springsocial.security;


import com.alibaba.fastjson.JSON;
import com.example.springsocial.api.Api;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.modelSSO.User;
import com.example.springsocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jorge santos neill on 15/11/19.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private Api obj = new Api();
	
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        /*User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
        );

        return UserPrincipal.create(user);
        */
    	return null;
    }

    @Transactional
    public UserDetails loadUserById(Long id, String authorizationHeader) {
    	User user=null;
    	try {
    		obj.setPath("user/me");
            obj.setAuthorizationHeader(authorizationHeader);
            obj.sendGet();
            user = JSON.parseObject(obj.getString().replace("company_id", "companyId"),User.class);  
    	}catch(Exception ex) {
    		throw new ResourceNotFoundException("User", "id", id);
    	}
        
        return UserPrincipal.create(user);
    }
}