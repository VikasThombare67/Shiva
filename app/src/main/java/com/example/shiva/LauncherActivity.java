package com.example.shiva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {

    private Button btnAdminLogin, btnStudentLogin, btnStudentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        btnAdminLogin = findViewById(R.id.btn_admin_login);
        btnStudentLogin = findViewById(R.id.btn_student_login);


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
