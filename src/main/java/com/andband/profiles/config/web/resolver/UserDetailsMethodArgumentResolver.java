package com.andband.profiles.config.web.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

public class UserDetailsMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private ObjectMapper objectMapper;

    public UserDetailsMethodArgumentResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserDetails.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        UserDetails tokenDetails = new UserDetails();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            if (oAuth2Authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails authenticationDetails = (OAuth2AuthenticationDetails) oAuth2Authentication.getDetails();

                String tokenString = authenticationDetails.getTokenValue();
                Jwt jwt = JwtHelper.decode(tokenString);

                Map<String, String> claims  = objectMapper.readValue(jwt.getClaims(), Map.class);
                String accountId = claims.get("accountId");
                String email = claims.get("user_name");

                tokenDetails.setAccountId(accountId);
                tokenDetails.setEmail(email);
            }
        }

        return tokenDetails;
    }
}
