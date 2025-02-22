package com.example.shiva.model;

public class Notice {
    private String id;
    private String title;
    private String description;
    private String department; // "All" means visible to everyone

    public Notice() {
        // Required empty constructor for Firestore
    }

    public Notice(String id, String title, String description, String department) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.department = department;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
