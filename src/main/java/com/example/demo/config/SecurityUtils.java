package com.example.demo.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.config.custom.UserDetailsCustom;

/*	1. The simplest way to retrieve the currently authenticated principal 
 * 		is via a static call to the SecurityContextHolder.
 *  2. When i want to check if they are logged in or not i use getSessionUsername method with username will return null or not.
 *  3. When user loggin successed (getSessionUsername() != null) i use the getPrincipal() method to get the user logging successed.
 *	4. I convert UserDetailsCustom to UserDto in UserService and use it in Controllers class.
  
 */

@Component
public class SecurityUtils {
    
    public static String getSessionUsername () {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String userName =authentication.getName();
		 	return userName;
		}
		return null;
		}

		public static UserDetailsCustom getPrincipal() {
			return (UserDetailsCustom)(SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
		}
		
		
}
