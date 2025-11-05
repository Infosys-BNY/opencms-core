package org.opencms.user.dto;

public class UserDto {
    
    private String userId;
    private String userName;
    private String userFirstname;
    private String userLastname;
    private String userEmail;
    private Long userLastlogin;
    private Integer userFlags;
    private String userOu;
    private Long userDatecreated;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getUserLastlogin() {
        return userLastlogin;
    }

    public void setUserLastlogin(Long userLastlogin) {
        this.userLastlogin = userLastlogin;
    }

    public Integer getUserFlags() {
        return userFlags;
    }

    public void setUserFlags(Integer userFlags) {
        this.userFlags = userFlags;
    }

    public String getUserOu() {
        return userOu;
    }

    public void setUserOu(String userOu) {
        this.userOu = userOu;
    }

    public Long getUserDatecreated() {
        return userDatecreated;
    }

    public void setUserDatecreated(Long userDatecreated) {
        this.userDatecreated = userDatecreated;
    }
}
