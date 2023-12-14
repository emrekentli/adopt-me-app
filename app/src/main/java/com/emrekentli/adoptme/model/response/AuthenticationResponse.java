package com.emrekentli.adoptme.model.response;

import java.util.List;

public class AuthenticationResponse {
    private List<String> roles;
    private String token;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(List<String> roles, String token) {
        this.roles = roles;
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
