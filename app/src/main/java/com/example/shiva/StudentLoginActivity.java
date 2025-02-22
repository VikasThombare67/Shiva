package com.example.shiva;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shiva.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "StudentPrefs";
    private static final String KEY_STUDENT_ID = "studentId";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DEPARTMENT = "department";
    private static final String TAG = "StudentLogin";

    TextView tvSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        tvSignup = findViewById(R.id.tv_signup);
        etEmail = findViewById(R.id.et_student_email);
        etPassword = findViewById(R.id.et_student_password);
        btnLogin = findViewById(R.id.btn_student_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateStudentLogin();
            }
        });
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentLoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void validateStudentLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch student credentials from Firestore
        db.collection("students")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.firestore.QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Student student = document.toObject(Student.class);
                                if (student != null && student.getPassword().equals(password)) {
                                    String studentId = document.getId(); // Get Firestore Document ID

                                    // Save student details in SharedPreferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_STUDENT_ID, studentId);
                                    editor.putString(KEY_EMAIL, student.getEmail());
                                    editor.putString(KEY_DEPARTMENT, student.getDepartment());
                                    editor.apply();

                                    Toast.makeText(StudentLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(StudentLoginActivity.this, StudentHomeActivity.class));
                                    finish();
                                    return;
                                }
                            }
                            Toast.makeText(StudentLoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(StudentLoginActivity.this, "Student Not Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
