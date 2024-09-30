package com.example.demo.config.custom;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

/*
 * 	1.Interface UserDetails is a most important of spring security. 
 * 		Implementations are not used directly by Spring Security for security purposes. 
 * 		They simply store user information which is later encapsulated into Authentication objects. 
 * 		This allows non-security related user information 
 * 		(such as email addresses, telephone numbers etc) to be stored in a convenient location.
 * 
 * 	2. UserDetails and UserDetailsService support each other so i wanna use UserDetailsService (have only one method loadUserByUsername) 
 * 		i make my UserDetailsCustom implements UserDetails.
 * 
 *	3.The UserDetailsCustom class used to save fields when user login success.  
 *
 *	BONUTE : The methods isAccountNonExpired() ,isEnabled(), isCredentialsNonExpired(),isAccountNonLocked() are default (true.).
 */

@Getter
@Setter
@Component
public class UserDetailsCustom implements UserDetails {

	private static final long serialVersionUID = 1L;

	private int id;
	private String username;
	private String password;
	private String fullName;
	private String nameCompany;
	private String avatar;
	private boolean status;
	private int idCompany;
	private List<GrantedAuthority> authorities;

	public UserDetailsCustom() {
	}

	public UserDetailsCustom(UserEntity entity) {
		this.id = entity.getId();
		this.username = entity.getEmail();
		this.password = entity.getPassword();
		this.fullName = entity.getFullName();
		this.avatar=entity.getAvatar();
		this.status = entity.isStatus();
		if (entity.getCompanyEntity() != null) {
			this.idCompany = entity.getCompanyEntity().getId();
			this.nameCompany = entity.getCompanyEntity().getName();
		}
		this.authorities = entity.getAuthorities().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
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
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "UserDetailsCustom [username=" + username + ", password=" + password + ", description="
				+ ", authorities=" + authorities + "]";
	}

}
