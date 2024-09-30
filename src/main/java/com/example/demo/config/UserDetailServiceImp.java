package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.config.custom.UserDetailsCustom;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.UserEntity;


/* 1. The interface UserDetailsService is a most important in spring security.
 * 2. UserDetailsService have only one method is loadUserByUsername. 
 * 		-It used to find user by username on database when user login.
 * 		-And then i use a object UserDetailsCustom's field of spring security 
 * 			to save user's information when logging successed.
 * 		-To getting object UserDetailsCustom, i use SecurityContextHolder.getContext().getAuthentication().getPrincipal()
 * 			in SecurityUtil class method UserDetailsCustom getPrincipal().
 * 	NEXT STEP : SecurityUtils :
 */

@Service
public class UserDetailServiceImp implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userDao.findUserByEmail(username).orElseThrow(()->new UsernameNotFoundException("Can't find email " + username));
        UserDetailsCustom userCustom = new UserDetailsCustom(userEntity);
        return userCustom;
    }
    
}
