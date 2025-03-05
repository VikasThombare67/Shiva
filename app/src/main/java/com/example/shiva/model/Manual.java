package com.example.shiva.model;

public class Manual {
    private String title;
    private String department;
    private String pdfUrl;

    // Required empty constructor for Firestore
    public Manual() {
    }

    public Manual(String title, String department, String pdfUrl) {
        this.title = title;
        this.department = department;
        this.pdfUrl = pdfUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}
