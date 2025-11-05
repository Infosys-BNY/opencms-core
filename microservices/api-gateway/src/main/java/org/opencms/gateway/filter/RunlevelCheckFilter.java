package org.opencms.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class RunlevelCheckFilter implements GlobalFilter, Ordered {
    
    private static final Logger logger = LoggerFactory.getLogger(RunlevelCheckFilter.class);
    
    private final WebClient.Builder webClientBuilder;
    
    public RunlevelCheckFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        String requestUrl = exchange.getRequest().getURI().toString();
        
        exchange.getAttributes().put("requestId", requestId);
        
        logger.info("Request {} - URL: {}", requestId, requestUrl);
        
        long startTime = System.currentTimeMillis();
        
        return chain.filter(exchange)
            .doFinally(signalType -> {
                long duration = System.currentTimeMillis() - startTime;
                logger.info("Request {} completed in {}ms - Status: {}", 
                    requestId, duration, exchange.getResponse().getStatusCode());
            });
    }
    
    private Mono<Boolean> checkServiceHealth(String serviceUrl) {
        return webClientBuilder.build()
            .get()
            .uri(serviceUrl + "/actuator/health")
            .retrieve()
            .toBodilessEntity()
            .map(response -> response.getStatusCode().is2xxSuccessful())
            .onErrorReturn(false);
    }
    
    @Override
    public int getOrder() {
        return -1;
    }
}
