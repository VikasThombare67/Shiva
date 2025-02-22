package com.example.shiva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {

    private Button btnAdminLogin, btnStudentLogin;
    private SharedPreferences adminPrefs, studentPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        btnAdminLogin = findViewById(R.id.btn_admin_login);
        btnStudentLogin = findViewById(R.id.btn_student_login);

        adminPrefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        studentPrefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);

        // Check if admin is logged in
        if (adminPrefs.getBoolean("isAdminLoggedIn", false)) {
            startActivity(new Intent(LauncherActivity.this, AdminDashboardActivity.class));
            finish();
            return; // Prevent further execution
        }

        // Check if student is logged in
        if (studentPrefs.getBoolean("isStudentLoggedIn", false)) {
            startActivity(new Intent(LauncherActivity.this, StudentHomeActivity.class));
            finish();
            return;
        }

        // If no one is logged in, show login options
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LauncherActivity.this, AdminLoginActivity.class));
            }
        });

        btnStudentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LauncherActivity.this, StudentLoginActivity.class));
            }
        });
    }
}
