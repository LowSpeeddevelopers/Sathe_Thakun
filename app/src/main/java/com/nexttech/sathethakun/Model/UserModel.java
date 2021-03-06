package com.nexttech.sathethakun.Model;

public class UserModel {
    private String userID;
    private String name;
    private String email;
    private String phone;
    private String age;
    private String address;
    private String imageUri;

    public UserModel() {
    }

    public UserModel(String userID, String name, String email, String phone, String age, String address, String imageUri) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.address = address;
        this.imageUri = imageUri;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
