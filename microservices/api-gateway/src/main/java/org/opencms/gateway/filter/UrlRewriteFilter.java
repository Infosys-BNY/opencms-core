package org.opencms.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class UrlRewriteFilter implements GlobalFilter, Ordered {
    
    private static final Logger logger = LoggerFactory.getLogger(UrlRewriteFilter.class);
    
    private static final List<String> EXCLUDE_PREFIXES = Arrays.asList(
        "/export",
        "/workplace",
        "/VAADIN",
        "/opencms",
        "/resources",
        "/webdav",
        "/webdav2",
        "/cmisatom",
        "/handle404",
        "/services",
        "/setup",
        "/update",
        "/handleBuiltinService"
    );
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        boolean shouldExclude = EXCLUDE_PREFIXES.stream()
            .anyMatch(path::startsWith);
        
        if (!shouldExclude && !path.startsWith("/opencms/")) {
            String rewrittenPath = "/opencms" + path;
            ServerHttpRequest modifiedRequest = request.mutate()
                .path(rewrittenPath)
                .build();
            
            logger.debug("Rewriting path from {} to {}", path, rewrittenPath);
            
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }
        
        return chain.filter(exchange);
    }
    
    @Override
    public int getOrder() {
        return -2;
    }
}
