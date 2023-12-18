package com.emrekentli.adoptme.model.request;

import com.emrekentli.adoptme.model.dto.Gender;


public class PostRequest {
    private  String ownerId;
    private  String title;
    private  String name;
    private  String description;
    private  String animalTypeId;
    private  String animalBreedId;
    private  Integer age;
    private  Boolean verified;
    private  Boolean status;
    private Gender gender;
    private  String cityId;
    private  String districtId;
    private  String mainImage;

    public PostRequest(String ownerId, String title, String name, String description, String animalTypeId, String animalBreedId, Integer age, Boolean verified, Boolean status, Gender gender, String cityId, String districtId, String mainImage) {
        this.ownerId = ownerId;
        this.title = title;
        this.name = name;
        this.description = description;
        this.animalTypeId = animalTypeId;
        this.animalBreedId = animalBreedId;
        this.age = age;
        this.verified = verified;
        this.status = status;
        this.gender = gender;
        this.cityId = cityId;
        this.districtId = districtId;
        this.mainImage = mainImage;
    }

    public PostRequest() {
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnimalTypeId() {
        return animalTypeId;
    }

    public void setAnimalTypeId(String animalTypeId) {
        this.animalTypeId = animalTypeId;
    }

    public String getAnimalBreedId() {
        return animalBreedId;
    }

    public void setAnimalBreedId(String animalBreedId) {
        this.animalBreedId = animalBreedId;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }
}
