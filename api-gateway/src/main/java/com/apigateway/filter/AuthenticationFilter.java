package com.apigateway.filter;


import com.apigateway.client.UserServiceClient;
import com.apigateway.error.ApiError;
import com.apigateway.exception.AuthException;
import feign.FeignException;
import jakarta.ws.rs.ext.ParamConverter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final UserServiceClient userServiceClient;

    private final RouteValidator validator;



    public AuthenticationFilter(@Lazy UserServiceClient userServiceClient, RouteValidator routeValidator) {
        super(Config.class);
        this.userServiceClient = userServiceClient;
        this.validator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    userServiceClient.validateToken(authHeader);
                }catch(FeignException fe){
                    throw new AuthException(fe.contentUTF8());
                }
                catch (Exception ex) {
                    // Hata mesajlarını loglayabilir veya yeniden fırlatabilirsiniz
                   System.out.println(ex);
                    throw ex;
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}