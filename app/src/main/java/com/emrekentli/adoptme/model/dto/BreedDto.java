package com.emrekentli.adoptme.model.dto;


import java.util.Date;

public class BreedDto {
    private  String id;
    private  Date created;
    private  Date modified;
    private  String name;
    private  AnimalTypeDto animalType;

    public BreedDto() {
    }

    public BreedDto(String id, Date created, Date modified, String name, AnimalTypeDto animalType) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.name = name;
        this.animalType = animalType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnimalTypeDto getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalTypeDto animalType) {
        this.animalType = animalType;
    }
}
