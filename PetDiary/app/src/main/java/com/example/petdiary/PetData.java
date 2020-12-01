package com.example.petdiary;

public class PetData {
    private String name;
    private String imageUrl;
    private String memo;
    private String petId;

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public PetData(String petId, String name, String imageUrl, String memo) {
        this.petId = petId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.memo = memo;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMemo() {
        return memo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
