package com.emrekentli.adoptme.model.dto;


import java.util.Date;


public class CityDto {
    private  String id;
    private  Date created;
    private  Date modified;
    private  Boolean status;
    private  String name;

    public CityDto() {
    }

    public CityDto(String id, Date created, Date modified, Boolean status, String name) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.status = status;
        this.name = name;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
