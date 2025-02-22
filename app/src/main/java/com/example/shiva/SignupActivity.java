package com.example.shiva;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shiva.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Spinner spinnerDepartment, spinnerSemester;
    private Button btnSignup;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        spinnerDepartment = findViewById(R.id.spinner_department);
        spinnerSemester = findViewById(R.id.spinner_semester);
        btnSignup = findViewById(R.id.btn_signup);

        // Populate Department Spinner
        List<String> departments = Arrays.asList("Computer", "IT", "Civil", "Mechanical", "Electronics");
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(deptAdapter);

        // Populate Semester Spinner
        List<String> semesters = Arrays.asList("FY", "SY", "TY");
        ArrayAdapter<String> semAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semesters);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(semAdapter);

        // Handle Signup Button Click
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String department = spinnerDepartment.getSelectedItem().toString();
        String semester = spinnerSemester.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user document with a generated ID
        DocumentReference userRef = db.collection("students").document();
        String userId = userRef.getId(); // Get the auto-generated document ID

        User user = new User(userId, name, email, password, department, semester);

        // Store data in Firestore
        userRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                    // Close activity after success
                } else {
                    Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
