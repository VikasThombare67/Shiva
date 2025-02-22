package com.example.shiva;

public class Staff {
    private String name;
    private String branch;
    private String contact;
    private String imageUrl;

    public Staff() {}  // Empty constructor (Firebase के लिए जरूरी)

    public Staff(String name, String branch, String contact, String imageUrl) {
        this.name = name;
        this.branch = branch;
        this.contact = contact;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getName() { return name; }
    public String getBranch() { return branch; }
    public String getContact() { return contact; }
    public String getImageUrl() { return imageUrl; }
}
