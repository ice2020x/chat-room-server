package com.ice.chatserver.pojo.config;

import lombok.Data;

@Data
public class JwtInfo {
    private String userId;
    private String username;
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public JwtInfo(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
    
    public JwtInfo() {
    }
}