package com.example.shiva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentHomeActivity extends AppCompatActivity {

    private CardView cardNotices, cardStaff;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        cardNotices = findViewById(R.id.card_notices);
        cardStaff = findViewById(R.id.card_staff);
        btnLogout = findViewById(R.id.btn_logout);

        // Open Student Dashboard
        cardNotices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentHomeActivity.this, StudentNoticesActivity.class));
            }
        });

        // Open Staff Activity
        cardStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentHomeActivity.this, NotesViewActivity.class));
            }
        });

        // Logout Button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear SharedPreferences (User session)
                SharedPreferences preferences = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                // Redirect to Student Login
                Intent intent = new Intent(StudentHomeActivity.this, StudentLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Close current activity
            }
        });
    }
}
