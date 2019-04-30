package com.andband.profiles.util;

import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

public class CustomClientCredentialsAccessTokenProvider extends ClientCredentialsAccessTokenProvider {

    private RestTemplate restTemplate;

    public CustomClientCredentialsAccessTokenProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        setMessageConverters(restTemplate.getMessageConverters());
    }

    @Override
    protected RestOperations getRestTemplate() {
        return restTemplate;
    }

}
