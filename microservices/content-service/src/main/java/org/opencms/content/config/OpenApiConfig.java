package org.opencms.content.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI contentServiceAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Content Service API")
                .description("OpenCms Content Service - VFS and content management operations")
                .version("1.0.0")
                .contact(new Contact()
                    .name("OpenCms Development Team")
                    .email("dev@opencms.org")));
    }
}
