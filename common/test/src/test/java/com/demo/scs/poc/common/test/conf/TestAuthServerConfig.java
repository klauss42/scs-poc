package com.demo.scs.poc.common.test.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class TestAuthServerConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	@SuppressWarnings("static-method")
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("user").password("password").authorities("user").and()
		.withUser("alice").password("password").authorities("user").and()
		.withUser("bob").password("password").authorities("user").and()
		.withUser("eve").password("password").authorities("user");
	}

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}
}
