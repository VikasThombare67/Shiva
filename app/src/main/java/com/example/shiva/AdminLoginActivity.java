package com.example.shiva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shiva.model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);

        etUsername = findViewById(R.id.et_admin_username);
        etPassword = findViewById(R.id.et_admin_password);
        btnLogin = findViewById(R.id.btn_admin_login);

        // Check if admin is already logged in
        if (sharedPreferences.getBoolean("isAdminLoggedIn", false)) {
            navigateToDashboard();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAdminLogin();
            }
        });
    }

    private void validateAdminLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch admin credentials from Firestore
        db.collection("admin").get().addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<com.google.firebase.firestore.QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    boolean isAuthenticated = false;
                    String adminId = "";

                    for (DocumentSnapshot document : task.getResult()) {
                        Admin admin = document.toObject(Admin.class);
                        if (admin != null && admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                            isAuthenticated = true;
                            adminId = document.getId(); // Store admin document ID
                            break;
                        }
                    }

                    if (isAuthenticated) {
                        Toast.makeText(AdminLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        // Save login details in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isAdminLoggedIn", true);
                        editor.putString("adminUsername", username);
                        editor.putString("adminId", adminId); // Store Admin ID for future reference
                        editor.apply();

                        navigateToDashboard();
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminLoginActivity.this, "No Admin Found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void navigateToDashboard() {
        startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
        finish();
    }
}
