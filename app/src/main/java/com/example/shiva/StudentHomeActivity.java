package com.example.shiva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentHomeActivity extends AppCompatActivity {

    private CardView cardNotices, cardStaff, cardNotes, cardReports;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        cardNotices = findViewById(R.id.card_notices);
        cardStaff = findViewById(R.id.card_staff);
        cardNotes = findViewById(R.id.card_notes);
        cardReports = findViewById(R.id.card_reports);
        btnLogout = findViewById(R.id.btn_logout);

        cardNotices.setOnClickListener(v -> startActivity(new Intent(StudentHomeActivity.this, StudentNoticesActivity.class)));
        cardStaff.setOnClickListener(v -> startActivity(new Intent(StudentHomeActivity.this, StaffViewActivity.class)));
        cardNotes.setOnClickListener(v -> startActivity(new Intent(StudentHomeActivity.this, NotesViewActivity.class)));
        cardReports.setOnClickListener(v -> startActivity(new Intent(StudentHomeActivity.this, ManualView.class)));

        btnLogout.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear().apply();

            Intent intent = new Intent(StudentHomeActivity.this, StudentLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
