package com.auth.server_auth.models;

import javax.json.bind.annotation.JsonbProperty;

public class UserCredentials {
    @JsonbProperty("username")
    private String username;

    @JsonbProperty("password")
    private String password;

    public UserCredentials() {}

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
