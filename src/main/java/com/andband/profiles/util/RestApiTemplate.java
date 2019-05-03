package com.andband.profiles.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RestApiTemplate {

    private RestTemplate restTemplate;
    private String apiUri;
    private HttpHeaders httpHeaders;

    public RestApiTemplate(RestTemplate restTemplate, String apiUri) {
        this.restTemplate = restTemplate;
        this.apiUri = apiUri;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public <T> T get(String path, Class<T> responseType) {
        return get(path, null, responseType);
    }

    public <T> T get(String path, Map<String, ?> uriVariables, Class<T> responseType) {
        String url = apiUri + path;
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

    public <T, K> T post(String path, K requestBody, Class<T> responseType) {
        return post(path, requestBody, responseType, null);
    }

    public <T> T post(String path, Map<String, ?> uriVariables, Class<T> responseType) {
        return post(path, null, responseType, uriVariables);
    }

    public <T, K> T post(String path, K requestBody, Class<T> responseType, Map<String, ?> params) {
        String url = apiUri + path;
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
