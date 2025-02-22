package com.example.shiva.model;

public class NoteModel {
    private String documentId; // Firestore Document ID
    private String department;
    private String fileUrl;
    private String fileName;
    private String originalFileName;

    public NoteModel(String documentId, String department, String fileUrl, String fileName, String originalFileName) {
        this.documentId = documentId;
        this.department = department;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    // Empty constructor required for Firestore
    public NoteModel() {
    }

    public NoteModel(String documentId, String department, String fileUrl, String fileName) {
        this.documentId = documentId;
        this.department = department;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
    }

    // Getters and Setters
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
