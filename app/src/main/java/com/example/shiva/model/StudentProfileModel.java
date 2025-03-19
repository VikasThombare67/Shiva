package com.example.shiva.model;

public class StudentProfileModel {
    private String fullName, email, mobile, branch, dob, aadhaar, bloodGroup, caste, category, city, gender, nationality, permanentAddress, pinCode, religion, state;

    // Empty Constructor (Required for Firestore)
    public StudentProfileModel() {}

    // Constructor
    public StudentProfileModel(String fullName, String email, String mobile, String branch, String dob, String aadhaar, String bloodGroup, String caste, String category, String city, String gender, String nationality, String permanentAddress, String pinCode, String religion, String state) {
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.branch = branch;
        this.dob = dob;
        this.aadhaar = aadhaar;
        this.bloodGroup = bloodGroup;
        this.caste = caste;
        this.category = category;
        this.city = city;
        this.gender = gender;
        this.nationality = nationality;
        this.permanentAddress = permanentAddress;
        this.pinCode = pinCode;
        this.religion = religion;
        this.state = state;
    }

    // Getters
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getBranch() { return branch; }
    public String getDob() { return dob; }
    public String getAadhaar() { return aadhaar; }
    public String getBloodGroup() { return bloodGroup; }
    public String getCaste() { return caste; }
    public String getCategory() { return category; }
    public String getCity() { return city; }
    public String getGender() { return gender; }
    public String getNationality() { return nationality; }
    public String getPermanentAddress() { return permanentAddress; }
    public String getPinCode() { return pinCode; }
    public String getReligion() { return religion; }
    public String getState() { return state; }
}
