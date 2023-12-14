package com.emrekentli.adoptme.model.response;


import java.util.Date;
public class UserDto {
    private  String id;
    private  Date created;
    private  Date modified;
    private  String password;
    private  String image;
    private  String fullName;
    private  String email;
    private  String phoneNumber;

    public UserDto() {
    }

    public UserDto(String id, Date created, Date modified, String password, String image, String fullName, String email, String phoneNumber) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.password = password;
        this.image = image;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
