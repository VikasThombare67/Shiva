package com.example.shiva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import java.util.ArrayList;
import java.util.List;

public class StudentHomeActivity extends AppCompatActivity {

    private CardView cardNotices, cardStaff, cardNotes, cardReports,cardprofile ,cardbook;
    private Button btnLogout;
    private ImageSlider imageSlider; // Image Slider Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        // Initialize UI Components
        imageSlider = findViewById(R.id.imageSlider);
        cardNotices = findViewById(R.id.card_notices);
        cardbook = findViewById(R.id.card_book);
        cardStaff = findViewById(R.id.card_staff);
        cardNotes = findViewById(R.id.card_notes);
        cardReports = findViewById(R.id.card_reports);
        btnLogout = findViewById(R.id.btn_logout);
        cardprofile= findViewById(R.id.card_profile);
        // 🔹 Setup Image Slider
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.clg, "Welcome to College", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.hod, "Head of computer dept", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.staffffff, "All staff of computer ", ScaleTypes.FIT));
       slideModels.add(new SlideModel(R.drawable.yash, "Devloper", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.sup, " all rounder & fullstack dev ", ScaleTypes.FIT));

        slideModels.add(new SlideModel(R.drawable.vikas, "Tester", ScaleTypes.FIT));
       slideModels.add(new SlideModel(R.drawable.shiva, "Designer", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.sama, "Thinker", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        // 🔹 Set Click Listeners
        cardNotices.setOnClickListener(v -> startActivity(new Intent(StudentHomeActivity.this, StudentNoticesActivity.class)));
        cardStaff.setOnClickListener(v -> startActivity(new Intent(StudentHomeActivity.this, StaffViewActivity.class)));
        cardNotes.setOnClickListener(v -> startActivity(new Intent(StudentHomeActivity.this, NotesViewActivity.class)));
        cardReports.setOnClickListener(v -> startActivity(new Intent(StudentHomeActivity.this, ManualView.class)));
        cardprofile.setOnClickListener(v -> startActivity(new Intent(StudentHomeActivity.this, item_profile.class)));
        cardbook.setOnClickListener(v -> startActivity(new Intent(StudentHomeActivity.this,activity_feedback.class)));
        // 🔹 Logout Functionality
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
