package com.andband.profiles;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.andband.profiles.util.CustomClientCredentialsAccessTokenProvider;
import com.andband.profiles.util.RestApiTemplate;
import com.andband.profiles.web.messages.MessageMapper;
import com.andband.profiles.web.profiles.ProfileMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@EnableCircuitBreaker
@EnableEurekaClient
@EnableOAuth2Client
@SpringBootApplication
public class ProfilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfilesApplication.class, args);
    }

    @Bean
    protected OAuth2ProtectedResourceDetails resource(@Value("${andband.auth.oauth.access-token-uri}") String accessTokenUri,
                                                      @Value("${andband.auth.client.internal-api.client-id}") String clientID,
                                                      @Value("${andband.auth.client.internal-api.client-secret}") String clientSecret) {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setAccessTokenUri(accessTokenUri);
        resource.setClientId(clientID);
        resource.setClientSecret(clientSecret);
        return resource;
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

    @LoadBalanced
    @Bean("oAuth2RestTemplate")
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ProtectedResourceDetails resourceDetails,
                                                 @Qualifier("restTemplate") RestTemplate restTemplate) {
        OAuth2ClientContext context = new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest());
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, context);

        AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(
                Collections.singletonList(
                        new CustomClientCredentialsAccessTokenProvider(restTemplate)
                )
        );
        oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);

        return oAuth2RestTemplate;
    }


    @Bean("profileImageBucket")
    public AmazonS3 profileImageBucket(@Value("${andband.aws.s3.profile-image.access-key}") String accessKey,
                                       @Value("${andband.aws.s3.profile-image.secret-key}") String secretKey) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build();
    }

    @Bean
    public ProfileMapper accountMapper() {
        return Mappers.getMapper(ProfileMapper.class);
    }

    @Bean
    public MessageMapper messageMapper() {
        return Mappers.getMapper(MessageMapper.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean("notificationApi")
    public RestApiTemplate notificationRestTemplate(@Qualifier("oAuth2RestTemplate") RestTemplate restTemplate,
                                                    @Value("${andband.notification-service.endpoint}") String notificationEndpoint) {
        return createRestApiTemplate(restTemplate, notificationEndpoint);
    }

    @Bean("accountsApi")
    public RestApiTemplate accountsRestTemplate(@Qualifier("oAuth2RestTemplate") RestTemplate restTemplate,
                                                @Value("${andband.accounts-api.endpoint}") String accountsApiEndpoint) {
        return createRestApiTemplate(restTemplate, accountsApiEndpoint);
    }

    private RestApiTemplate createRestApiTemplate(RestTemplate restTemplate, String apiEndpoint) {
        RestApiTemplate restApiTemplate = new RestApiTemplate(restTemplate, apiEndpoint);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restApiTemplate.setHttpHeaders(headers);
        return restApiTemplate;
    }

}
