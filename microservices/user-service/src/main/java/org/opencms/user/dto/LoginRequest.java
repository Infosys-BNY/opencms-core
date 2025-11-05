package org.opencms.user.dto;

public class LoginRequest {
    
    private String username;
    private String password;
    private String secondFactorCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecondFactorCode() {
        return secondFactorCode;
    }

    public void setSecondFactorCode(String secondFactorCode) {
        this.secondFactorCode = secondFactorCode;
    }
}
