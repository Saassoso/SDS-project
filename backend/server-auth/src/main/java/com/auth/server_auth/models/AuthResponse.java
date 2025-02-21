package com.auth.server_auth.models;

public class AuthResponse {
    private boolean success;
    
    public AuthResponse() {}
    
    public AuthResponse(boolean success) { 
        this.success = success; 
    }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}