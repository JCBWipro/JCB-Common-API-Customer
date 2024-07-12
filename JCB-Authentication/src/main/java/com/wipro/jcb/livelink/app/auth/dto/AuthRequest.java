package com.wipro.jcb.livelink.app.auth.dto;

public class AuthRequest {

    private String username;
    private String password;
    private String role;

    AuthRequest() {
    }

    public AuthRequest(String username, String password, String role) {
        super();
        this.username = username;
        this.password = password;
        this.role = role;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
