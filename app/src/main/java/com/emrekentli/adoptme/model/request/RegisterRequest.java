package com.emrekentli.adoptme.model.request;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;

    public RegisterRequest(String fullName, String email, String password, String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public RegisterRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
