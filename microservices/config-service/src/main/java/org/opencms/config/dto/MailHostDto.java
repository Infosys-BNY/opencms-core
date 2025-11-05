package org.opencms.config.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MailHostDto {
    private Long id;
    
    @NotNull
    private String name;
    
    @NotNull
    private Integer port;
    
    @NotNull
    private String protocol;
    
    private String username;
    private String password;
    private Integer orderIndex;
    private String mailFrom;
}
