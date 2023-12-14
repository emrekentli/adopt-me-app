package com.emrekentli.adoptme.model.dto;


import java.util.Date;


public class DistrictDto {
    private  String id;
    private  Date created;
    private  Date modified;
    private  CityDto city;
    private  Boolean status;
    private  String name;

    public DistrictDto(String id, Date created, Date modified, CityDto city, Boolean status, String name) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.city = city;
        this.status = status;
        this.name = name;
    }

    public DistrictDto() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void setCity(CityDto city) {
        this.city = city;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public CityDto getCity() {
        return city;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
