package com.example.shiva;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    TextView tvSignup, tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvSignup = findViewById(R.id.tv_signup);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);  // Forgot Password ID
        etEmail = findViewById(R.id.et_student_email);
        etPassword = findViewById(R.id.et_student_password);
        btnLogin = findViewById(R.id.btn_student_login);

        // Login button listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateStudentLogin();
            }
        });

        // Sign Up button listener
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentLoginActivity.this, SignupActivity.class));
            }
        });

        // Forgot Password listener
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog();  // Dialog to enter email
            }
        });
    }

    // Method to show Forgot Password dialog
    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        final EditText input = new EditText(this);
        input.setHint("Enter your registered email");
        builder.setView(input);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(StudentLoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(email);  // Reset password via Firebase
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    // Firebase password reset method
    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(StudentLoginActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StudentLoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

        // Firebase login
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    db.collection("students").document(user.getUid()).get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful() && task1.getResult().exists()) {
                                    Toast.makeText(StudentLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(StudentLoginActivity.this, StudentHomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(StudentLoginActivity.this, "Student Not Found!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Toast.makeText(StudentLoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
