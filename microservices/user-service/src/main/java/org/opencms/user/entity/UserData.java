package org.opencms.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CMS_USERDATA")
@IdClass(UserDataId.class)
public class UserData {
    
    @Id
    @Column(name = "USER_ID", length = 36)
    private String userId;
    
    @Id
    @Column(name = "DATA_KEY")
    private String dataKey;
    
    @Column(name = "DATA_VALUE", columnDefinition = "TEXT")
    private String dataValue;
    
    @Column(name = "DATA_TYPE")
    private String dataType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User user;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
