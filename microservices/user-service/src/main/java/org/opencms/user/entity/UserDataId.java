package org.opencms.user.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserDataId implements Serializable {
    
    private String userId;
    private String dataKey;
    
    public UserDataId() {
    }
    
    public UserDataId(String userId, String dataKey) {
        this.userId = userId;
        this.dataKey = dataKey;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDataId that = (UserDataId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(dataKey, that.dataKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, dataKey);
    }
}
