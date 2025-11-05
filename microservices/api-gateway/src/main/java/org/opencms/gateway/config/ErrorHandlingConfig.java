package org.opencms.gateway.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Configuration
public class ErrorHandlingConfig {
    
    @Bean
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(ServerRequest request, 
                                                         ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
                
                Integer status = (Integer) errorAttributes.get("status");
                if (status != null) {
                    if (status == 404) {
                        errorAttributes.put("redirectPath", "/opencms-errorhandler/handle404");
                    } else if (status == 500) {
                        errorAttributes.put("redirectPath", "/opencms-errorhandler/handle500");
                    }
                }
                
                return errorAttributes;
            }
        };
    }
}
