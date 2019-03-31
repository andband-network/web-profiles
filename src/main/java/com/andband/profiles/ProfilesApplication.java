package com.andband.profiles;

import com.andband.profiles.web.ProfileMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

@EnableCircuitBreaker
@EnableEurekaClient
@EnableOAuth2Client
@SpringBootApplication
public class ProfilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfilesApplication.class, args);
    }

    @Bean
    public ProfileMapper accountMapper() {
        return Mappers.getMapper(ProfileMapper.class);
    }

    @LoadBalanced
    @Bean("restTemplate")
    public RestTemplate loadBalancedRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @Primary
    public RemoteTokenServices tokenService(@Qualifier("restTemplate") RestTemplate restTemplate,
                                            @Value("${andband.auth.oauth.check-token-uri}") String checkTokenEndpoint,
                                            @Value("${andband.auth.client.internal-api.client-id}") String clientId,
                                            @Value("${andband.auth.client.internal-api.client-secret}") String clientSecret) {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(checkTokenEndpoint);
        tokenService.setClientId(clientId);
        tokenService.setClientSecret(clientSecret);
        tokenService.setRestTemplate(restTemplate);
        return tokenService;
    }

}
