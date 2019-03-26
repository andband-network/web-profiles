package com.andband.profiles.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${andband.auth.oauth.check-token-uri}")
    private String checkTokenEndpoint;

    @Value("${andband.auth.client.internal-api.client-id}")
    private String clientId;

    @Value("${andband.auth.client.internal-api.cilent-secret}")
    private String clientSecret;

    @Bean
    @Primary
    public RemoteTokenServices tokenService(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate) {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(checkTokenEndpoint);
        tokenService.setClientId(clientId);
        tokenService.setClientSecret(clientSecret);
        tokenService.setRestTemplate(restTemplate);
        return tokenService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/profiles/**").permitAll()
                .antMatchers(HttpMethod.POST, "/profiles/**").hasAuthority("ROLE_INTERNAL_API")
                .anyRequest().authenticated();
    }

}
