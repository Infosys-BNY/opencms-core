package org.opencms.publishing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PublishingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PublishingServiceApplication.class, args);
    }
}
