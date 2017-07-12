package com.demo.scs.poc.common.test.conf;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class TestAuthServer extends AuthorizationServerConfigurerAdapter {
//	@Value("${resource-server-testing.oauth2.jwt.private-key-value}")
//	private String	signing;
//	@Value("${security.oauth2.resource.jwt.key-value}")
//	private String	verifier;

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() throws Exception {
		JwtAccessTokenConverter jwt = new JwtAccessTokenConverter();
		jwt.setSigningKey("1234");
//		jwt.setSigningKey("signing");
//		jwt.setVerifierKey("verifier");
		jwt.afterPropertiesSet();
		return jwt;
	}

	@Autowired
	private AuthenticationManager	authenticationManager;

	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
		.authenticationManager(authenticationManager)
		.accessTokenConverter(accessTokenConverter());
	}

	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		.withClient("myclientwith")
		.authorizedGrantTypes("password")
		.authorities("myauthorities")
//		.resourceIds("myresource")
		.scopes("myscope")

		.and()
		.withClient("myclientwithout")
		.authorizedGrantTypes("password")
		.authorities("myauthorities")
//		.resourceIds("myresource")
		.scopes(UUID.randomUUID().toString());
	}
}


