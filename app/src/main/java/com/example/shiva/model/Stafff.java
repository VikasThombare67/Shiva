package com.example.shiva.model;

public class Stafff {
    private String id;
    private String name;
    private String department;
    private String contact;
    private String imageUrl;

    // 🔄 Default Constructor (Firebase साठी आवश्यक)
    public Stafff() {
    }

    // 🔄 Parameterized Constructor
    public Stafff(String id, String name, String department, String contact, String imageUrl) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.contact = contact;
        this.imageUrl = imageUrl;
    }

    // 🔄 Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getContact() {
        return contact;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // 🔄 Setters (Firebase साठी आवश्यक)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
