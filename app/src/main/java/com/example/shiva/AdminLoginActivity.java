package com.example.shiva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private TextInputEditText usernameEditText, passwordEditText;
    private TextInputLayout usernameLayout, passwordLayout;
    private Button loginButton;

    // Fixed admin username and password
    private static final String ADMIN_USERNAME = "admin";  // Fixed username
    private static final String ADMIN_PASSWORD = "admin123";  // Fixed password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        usernameLayout = (TextInputLayout) findViewById(R.id.username).getParent().getParent();
        passwordLayout = (TextInputLayout) findViewById(R.id.password).getParent().getParent();
        loginButton = findViewById(R.id.login_button);

        // Set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });
    }

    // Method to validate login credentials
    private void validateLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Reset errors
        usernameLayout.setError(null);
        passwordLayout.setError(null);

        // Username validation
        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError("Username is required");
            usernameEditText.requestFocus();
            return;
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }
        // Check if the entered credentials match the fixed admin credentials
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            // Successful login
            Toast.makeText(AdminLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

            // Start AdminHomeActivity on successful login
            Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Invalid credentials
            Toast.makeText(AdminLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
