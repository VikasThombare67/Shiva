package com.example.shiva.model;

public class Note {
    private String department;
    private String fileUrl;

    public Note() {
        // Required empty constructor for Firestore
    }

    public Note(String department, String fileUrl) {
        this.department = department;
        this.fileUrl = fileUrl;
    }

    public String getDepartment() {
        return department;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
