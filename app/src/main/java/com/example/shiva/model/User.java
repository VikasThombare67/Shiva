package com.example.shiva.model;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String department;
    private String semester;

    public User() {
        // Default constructor required for Firestore
    }

    public User(String id, String name, String email, String password, String department, String semester) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.semester = semester;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}
