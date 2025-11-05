package org.opencms.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationMessageDTO {
    
    private String message;
    private String help;
    
    public ValidationMessageDTO() {}
    
    public ValidationMessageDTO(String message, String help) {
        this.message = message;
        this.help = help;
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getHelp() { return help; }
    public void setHelp(String help) { this.help = help; }
}
