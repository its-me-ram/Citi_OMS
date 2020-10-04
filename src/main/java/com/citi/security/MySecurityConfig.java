//package com.citi.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class MySecurityConfig extends WebSecurityConfigurerAdapter{
//
//	@Bean
//	  public BCryptPasswordEncoder bCryptPasswordEncoder() {
//	      return new BCryptPasswordEncoder();
//	  }
//
//	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		// TODO Auto-generated method stub
//		auth.inMemoryAuthentication().withUser("admin").password("$2y$12$DLZZ9dgbOhdhQbMxd1F02e9IVit9ptqnInAtkYdKqAU5OJeA3sIQq").roles("admin").and().withUser("abc").password("").roles("guest","user");
//	}
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// TODO Auto-generated method stub
//		
//		http.httpBasic().and().authorizeRequests().antMatchers(HttpMethod.POST,"/orders").hasRole("admin").and().csrf().disable().formLogin().disable();
//		http.httpBasic().and().authorizeRequests().antMatchers(HttpMethod.POST,"/executed").hasRole("admin").and().csrf().disable().formLogin().disable();
//		http.httpBasic().and().authorizeRequests().antMatchers(HttpMethod.POST,"/rejected").hasRole("admin").and().csrf().disable().formLogin().disable();
//
//
//	}
//}
