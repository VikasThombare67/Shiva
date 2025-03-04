package com.example.shiva;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;


public class AdminDashboardActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        gridLayout = findViewById(R.id.grid_layout);
        btnLogout = findViewById(R.id.btn_logout);

        // Click Listeners for Cards
        setupCardClickListeners();

        // Logout Button Click
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear SharedPreferences
                SharedPreferences preferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                // Redirect to Admin Login
                Intent intent = new Intent(AdminDashboardActivity.this, AdminLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupCardClickListeners() {
        // Notice Management
        ((CardView) gridLayout.getChildAt(0)).setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, AdminNoticeActivity.class)));

        // Staff Management
        ((CardView) gridLayout.getChildAt(1)).setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, StaffUploadActivity.class)));

        // Student Management
        ((CardView) gridLayout.getChildAt(2)).setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, NotesUploadActivity.class)));

//        // Reports
//        ((CardView) gridLayout.getChildAt(3)).setOnClickListener(v ->
//                startActivity(new Intent(AdminDashboardActivity.this, ReportsActivity.class)));
    }
}
