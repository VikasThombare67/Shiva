// AdminLoginActivity.java
package com.example.shiva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    // Static admin credentials (hard-coded)
    private final String STATIC_USERNAME = "admin";
    private final String STATIC_PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Auto login check
        SharedPreferences preferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
            finish();
        }

        etUsername = findViewById(R.id.et_admin_username);
        etPassword = findViewById(R.id.et_admin_password);
        btnLogin = findViewById(R.id.btn_admin_login);

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

        if (username.equals(STATIC_USERNAME) && password.equals(STATIC_PASSWORD)) {
            // Save login session
            SharedPreferences preferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Toast.makeText(AdminLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
            navigateToDashboard();
        } else {
            Toast.makeText(AdminLoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToDashboard() {
        startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
        finish();
    }
}
