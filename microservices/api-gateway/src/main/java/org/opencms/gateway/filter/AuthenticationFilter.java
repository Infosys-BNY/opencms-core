package org.opencms.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    
    public AuthenticationFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorizedResponse(exchange);
            }
            
            String token = authHeader.substring(7);
            
            if (!validateToken(token)) {
                return unauthorizedResponse(exchange);
            }
            
            String userId = extractUserIdFromToken(token);
            
            ServerWebExchange modifiedExchange = exchange.mutate()
                .request(r -> r.header("X-User-Id", userId)
                              .header("X-Auth-Token", token))
                .build();
            
            return chain.filter(modifiedExchange);
        };
    }
    
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    
    private boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    private String extractUserIdFromToken(String token) {
        return "user-from-token";
    }
    
    public static class Config {
    }
}
