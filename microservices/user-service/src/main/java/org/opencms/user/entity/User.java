package org.opencms.user.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "CMS_USERS")
public class User {
    
    @Id
    @Column(name = "USER_ID", length = 36)
    private String userId;
    
    @Column(name = "USER_NAME", nullable = false, unique = true)
    private String userName;
    
    @Column(name = "USER_PASSWORD", nullable = false)
    private String userPassword;
    
    @Column(name = "USER_FIRSTNAME")
    private String userFirstname;
    
    @Column(name = "USER_LASTNAME")
    private String userLastname;
    
    @Column(name = "USER_EMAIL")
    private String userEmail;
    
    @Column(name = "USER_LASTLOGIN")
    private Long userLastlogin;
    
    @Column(name = "USER_FLAGS")
    private Integer userFlags;
    
    @Column(name = "USER_OU")
    private String userOu;
    
    @Column(name = "USER_DATECREATED")
    private Long userDatecreated;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserData> additionalInfo = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "CMS_GROUPUSERS",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "GROUP_ID")
    )
    private Set<Group> groups = new HashSet<>();
    
    public User() {
        this.userId = UUID.randomUUID().toString();
        this.userDatecreated = System.currentTimeMillis();
    }

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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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

    public Set<UserData> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Set<UserData> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
}
