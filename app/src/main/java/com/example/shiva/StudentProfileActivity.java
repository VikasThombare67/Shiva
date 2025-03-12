package com.example.shiva;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class StudentProfileActivity extends AppCompatActivity {

    private EditText etFullName, etDOB, etBloodGroup, etAadhaar, etNationality, etCategory, etReligion, etCaste, etPermanentAddress,
            etCity, etState, etPinCode, etMobile, etEmail, etGuardianName, etGuardianContact, etGuardianOccupation,etBranch,
            etSSC_School, etSSC_Board, etSSC_Year, etSSC_Percentage,
            etHSC_College, etHSC_Board, etHSC_Year, etHSC_Percentage;
    private Spinner spinnerGender;
    private ImageView imgProfile;
    private Button btnUpdateProfile;
    private Uri imageUri;

    private FirebaseFirestore db;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private String userId;
    private ProgressDialog progressDialog;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("student_images");

        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initialize UI elements
        etBranch=findViewById(R.id.etBranch);
        imgProfile = findViewById(R.id.imgProfile);
        etFullName = findViewById(R.id.etFullName);
        etDOB = findViewById(R.id.etDOB);
        spinnerGender = findViewById(R.id.spinnerGender);
        etBloodGroup = findViewById(R.id.etBloodGroup);
        etAadhaar = findViewById(R.id.etAadhaar);
        etNationality = findViewById(R.id.etNationality);
        etCategory = findViewById(R.id.etCategory);
        etReligion = findViewById(R.id.etReligion);
        etCaste = findViewById(R.id.etCaste);
        etPermanentAddress = findViewById(R.id.etPermanentAddress);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etPinCode = findViewById(R.id.etPinCode);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etGuardianName = findViewById(R.id.etGuardianName);
        etGuardianContact = findViewById(R.id.etGuardianContact);
        etGuardianOccupation = findViewById(R.id.etGuardianOccupation);
        etSSC_School = findViewById(R.id.etSSC_School);
        etSSC_Board = findViewById(R.id.etSSC_Board);
        etSSC_Year = findViewById(R.id.etSSC_Year);
        etSSC_Percentage = findViewById(R.id.etSSC_Percentage);
        etHSC_College = findViewById(R.id.etHSC_College);
        etHSC_Board = findViewById(R.id.etHSC_Board);
        etHSC_Year = findViewById(R.id.etHSC_Year);
        etHSC_Percentage = findViewById(R.id.etHSC_Percentage);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        progressDialog = new ProgressDialog(this);

        // Select Profile Image
        imgProfile.setOnClickListener(view -> openGallery());

        // Update Profile
        btnUpdateProfile.setOnClickListener(view -> updateProfile());
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgProfile.setImageURI(imageUri);
        }
    }

    private void updateProfile() {
        if (!validateInputs()) return;

        progressDialog.setMessage("Updating profile...");
        progressDialog.show();

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("Enter Branch", etBranch.getText().toString());
        profileData.put("fullName", etFullName.getText().toString());
        profileData.put("dob", etDOB.getText().toString());
        profileData.put("gender", spinnerGender.getSelectedItem().toString());
        profileData.put("bloodGroup", etBloodGroup.getText().toString());
        profileData.put("aadhaar", etAadhaar.getText().toString());
        profileData.put("nationality", etNationality.getText().toString());
        profileData.put("category", etCategory.getText().toString());
        profileData.put("religion", etReligion.getText().toString());
        profileData.put("caste", etCaste.getText().toString());
        profileData.put("permanentAddress", etPermanentAddress.getText().toString());
        profileData.put("city", etCity.getText().toString());
        profileData.put("state", etState.getText().toString());
        profileData.put("pinCode", etPinCode.getText().toString());
        profileData.put("mobile", etMobile.getText().toString());
        profileData.put("email", etEmail.getText().toString());

        db.collection("students").document(userId)
                .set(profileData)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Update Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("FirestoreError", "Error updating profile", e);
                });
    }

    private boolean validateInputs() {
        return validateField(etFullName, "Full Name is required") &&
                validateField(etDOB, "Date of Birth is required") &&
                validateField(etBloodGroup, "Blood Group is required") &&
                validateField(etAadhaar, "Aadhaar Number is required") &&
                validateField(etNationality, "Nationality is required") &&
                validateField(etCategory, "Category is required") &&
                validateField(etReligion, "Religion is required") &&
                validateField(etCaste, "Caste is required") &&
                validateField(etPermanentAddress, "Permanent Address is required") &&
                validateField(etCity, "City is required") &&
                validateField(etState, "State is required") &&
                validateField(etPinCode, "Pin Code is required") &&
                validateField(etMobile, "Mobile Number is required") &&
                validateField(etEmail, "Email is required") &&
                validateField(etGuardianName, "Guardian Name is required") &&
                validateField(etGuardianContact, "Guardian Contact is required") &&
                validateField(etGuardianOccupation, "Guardian Occupation is required");
    }

    private boolean validateField(EditText field, String errorMessage) {
        if (field.getText().toString().trim().isEmpty()) {
            field.setError(errorMessage);
            field.requestFocus();
            return false;
        }
        return true;
    }
}
