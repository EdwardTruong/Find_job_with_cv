package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
/*
 * 1. The configureGlobal method finds the user in databases.
 * 		AuthenticationManagerBuilder get user from database when login and passwordEncoder is encoder password. 
 * 2. The filterChain method is authorize for user.
 * 
 * 
 */

@Configuration
@EnableWebSecurity
public class SecurityConfigurer  {

	@Autowired
	private UserDetailServiceImp userDetailServiceImp;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
		System.out.println("Check in from Security Configurer : configureGlobal");
		builder.userDetailsService(userDetailServiceImp).passwordEncoder(passwordEncoder());
	}

	@Bean
	public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
		// Use csrf : 
		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
		// set the name of the attribute the CsrfToken will be populated on
		// using with : .csrfTokenRequestHandler(requestHandler)
		requestHandler.setCsrfRequestAttributeName("_csrf"); 
		
		
		http.csrf(
//				(csrf) -> csrf.disable()
//				  (csrf) ->
//				  csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository()) // lưu trong session
				
				(csrf) -> csrf
				.csrfTokenRequestHandler(requestHandler)
				
//				csrf -> csrf
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // sử dụng qua Cookie và cho phép js can thiệp. Tạm thời cm lại để test sau trên các scrit.
//                .requireCsrfProtectionMatcher(new RegexRequestMatcher(".*", "POST|PUT|DELETE|PATCH")) // Toàn diện. Fix sau tạm thời cm lại
				
				  )
		.authorizeHttpRequests(requests -> requests
				.requestMatchers(HttpMethod.GET, "/assets/css/**", "/assets/fonts/**", "/assets/images/**","/assets/upload/**",
						"/assets/js/**", "/assets/new/**", "/assets/scss/**","/assets/cvs/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/user/uploadAvatar/**").hasAnyRole("USER","COMPANY")
				
				.requestMatchers(HttpMethod.GET, "/assets/new/css/**", "/assets/new/js/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/", "/auth/register", "/jobs/**", "/recruitment/detail/**",
						"/detail-company/**", "/test/**")
				.permitAll()
				
				.requestMatchers(HttpMethod.POST, "/test/**").permitAll()
				.requestMatchers(HttpMethod.GET	, "/user/files/**", "/user/pdf/**").permitAll()
				.requestMatchers(HttpMethod.GET	, "/user/company-posts/**").hasAnyRole("USER","COMPANY")
				.requestMatchers(HttpMethod.GET	, "/user/search/**").hasAnyRole("USER","COMPANY")
				.requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
				.requestMatchers(HttpMethod.POST, "/save").permitAll()
				// DONE FOR ALL
				.requestMatchers(HttpMethod.GET , "/company/**").hasRole("COMPANY")
				.requestMatchers(HttpMethod.GET , "/user/**").hasRole("USER")
				
				.requestMatchers(HttpMethod.GET , "/recruitment/detail/**").permitAll()
				.requestMatchers(HttpMethod.GET , "/recruitment/delete/**").hasAnyRole("COMPANY")
				.requestMatchers(HttpMethod.GET , "/recruitment/category/**", "/recruitment/search/**","/recruitment/searchaddress/**").hasAnyRole("USER","COMPANY")
				.requestMatchers(HttpMethod.GET , "/recruitment/**").hasAnyRole("USER","COMPANY")
				
				.requestMatchers(HttpMethod.POST, "/recruitment/**").hasRole("COMPANY")
				

				.requestMatchers(HttpMethod.GET	, "/save-job/get-list").hasAnyRole("USER","COMPANY")
				.requestMatchers(HttpMethod.GET	, "/follow/get-list").hasAnyRole("USER","COMPANY")
				.requestMatchers(HttpMethod.GET	, "/apply/get-list").hasAnyRole("USER","COMPANY")
				
				
				.requestMatchers(HttpMethod.GET	, "/save-job/delete/**", "/follow/delete-follow/**", "apply/delete/**").hasRole("USER")
				.requestMatchers(HttpMethod.POST, "/save-job/save/**", "/follow/follow-company/**").hasRole("USER")
				.requestMatchers(HttpMethod.POST, "/apply/apply-job1/**", "/apply/apply-job/**").hasRole("USER")
				.requestMatchers(HttpMethod.POST, "/apply/access/**").hasRole("COMPANY")
				.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/auth/login")
						.failureUrl("/auth/login-error")
						.loginProcessingUrl("/auth/processLogin")
						.defaultSuccessUrl("/home", true)
						.permitAll())

				.logout(logout -> {
					logout.invalidateHttpSession(true);
					logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout")).permitAll();
					logout.logoutSuccessUrl("/");
					logout.deleteCookies("JSESSIONID");
				})
				
				.exceptionHandling(handling -> {
					handling.accessDeniedPage("/access_denied");
	
				});


		return http.build();
	}



}