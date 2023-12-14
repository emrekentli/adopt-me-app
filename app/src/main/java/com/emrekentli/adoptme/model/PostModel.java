package com.emrekentli.adoptme.model;

import com.emrekentli.adoptme.model.dto.AnimalTypeDto;
import com.emrekentli.adoptme.model.dto.BreedDto;
import com.emrekentli.adoptme.model.dto.CityDto;
import com.emrekentli.adoptme.model.dto.DistrictDto;
import com.emrekentli.adoptme.model.dto.Gender;
import com.emrekentli.adoptme.model.response.UserDto;

import java.util.Date;
import java.util.Set;

public class PostModel {

    private String id;
    private Date created;
    private Date modified;
    private UserDto owner;
    private String title;
    private String name;
    private String description;
    private AnimalTypeDto animalType;
    private Gender gender;
    private BreedDto breed;
    private Integer age;
    private Boolean verified;
    private Boolean status;
    private CityDto city;
    private DistrictDto district;
    private String mainImage;
    private Set<String> images;

    public PostModel() {
    }

    public PostModel(String id, Date created, String name, Date modified, UserDto owner, String title, String description, AnimalTypeDto animalType, Gender gender, BreedDto breed, Integer age, Boolean verified, Boolean status, CityDto city, DistrictDto district, String mainImage, Set<String> images) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.name = name;
        this.animalType = animalType;
        this.gender = gender;
        this.breed = breed;
        this.age = age;
        this.verified = verified;
        this.status = status;
        this.city = city;
        this.district = district;
        this.mainImage = mainImage;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AnimalTypeDto getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalTypeDto animalType) {
        this.animalType = animalType;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BreedDto getBreed() {
        return breed;
    }

    public void setBreed(BreedDto breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public CityDto getCity() {
        return city;
    }

    public void setCity(CityDto city) {
        this.city = city;
    }

    public DistrictDto getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDto district) {
        this.district = district;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }
}