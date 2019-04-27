package com.andband.profiles.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableWebSecurity
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/profiles/**").authenticated()
                .antMatchers(HttpMethod.GET, "/profiles").authenticated()
                .antMatchers(HttpMethod.POST, "/profiles").hasAuthority("ROLE_INTERNAL_API")
                .antMatchers(HttpMethod.GET, "/profiles/search").permitAll()
                .antMatchers(HttpMethod.GET, "/profiles/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

}
