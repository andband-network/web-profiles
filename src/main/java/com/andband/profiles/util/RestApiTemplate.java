package com.andband.profiles.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RestApiTemplate {

    private RestTemplate restTemplate;
    private String restUri;
    private HttpHeaders httpHeaders;

    public RestApiTemplate(RestTemplate restTemplate, String restUri) {
        this.restTemplate = restTemplate;
        this.restUri = restUri;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public <T> T get(String resourceUrl, Class<T> responseType) {
        return get(resourceUrl, null, responseType);
    }

    public <T> T get(String resourceUrl, Map<String, ?> uriVariables, Class<T> responseType) {
        String url = restUri + resourceUrl;
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<T> response;

        if (uriVariables == null) {
            response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType);
        } else {
            response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType, uriVariables);
        }

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new RestApiTemplateException("returned http status code: " + response.getStatusCode() + " from url: " + url);
        }

        return response.getBody();
    }

    public <T, K> T post(String resourceUrl, K requestBody, Class<T> responseType) {
        return post(resourceUrl, requestBody, responseType, null);
    }

    public <T> T post(String resourceUri, Map<String, ?> uriVariables, Class<T> responseType) {
        return post(resourceUri, null, responseType, uriVariables);
    }

    public <T, K> T post(String resourceUri, K requestBody, Class<T> responseType, Map<String, ?> params) {
        String url = restUri + resourceUri;
        HttpEntity<K> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        ResponseEntity<T> response;

        if (params == null) {
            response = restTemplate.postForEntity(url, httpEntity, responseType);
        } else {
            response = restTemplate.postForEntity(url, httpEntity, responseType, params);
        }

        if (!isSuccess(response.getStatusCode())) {
            throw new RestApiTemplateException("returned http status code: " + response.getStatusCode() + " from url: " + url);
        }

        return response.getBody();
    }

    private boolean isSuccess(HttpStatus httpStatus) {
        switch (httpStatus) {
            case OK:
            case CREATED:
            case NO_CONTENT:
                return true;
            default:
                return false;
        }
    }

}
