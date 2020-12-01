package com.example.petdiary;

public class PetData {
    private String name;
    private String imageUrl;
    private String memo;


    public PetData(String name, String imageUrl, String memo) {
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
