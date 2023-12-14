package com.emrekentli.adoptme.model.dto;


import java.util.Date;

public class AnimalTypeDto {
    private  String id;
    private  Date created;
    private  Date modified;
    private  String name;

    public AnimalTypeDto() {
    }

    public AnimalTypeDto(String id, Date created, Date modified, String name) {
        this.id = id;
        this.created = created;
        this.modified = modified;
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

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }


}
