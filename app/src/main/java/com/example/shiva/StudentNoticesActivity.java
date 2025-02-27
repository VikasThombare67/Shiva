package com.example.shiva;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.shiva.adapter.NoticeAdapter;
import com.example.shiva.model.Notice;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class StudentNoticesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotices;
    private NoticeAdapter noticeAdapter;
    private List<Notice> noticeList;
    private FirebaseFirestore db;
    private String studentDepartment;
    private static final String TAG = "StudentDashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notices);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

// ✅ Firestore Offline Mode Enable Kara
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Local caching allow karto
                .build();
        db.setFirestoreSettings(settings);

        // Get student details from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);
        studentDepartment = sharedPreferences.getString("department", "All");

        Log.d(TAG, "Student Department (from SharedPreferences): " + studentDepartment);

        // Setup RecyclerView
        recyclerViewNotices = findViewById(R.id.recyclerViewNotices);
        recyclerViewNotices.setLayoutManager(new LinearLayoutManager(this));

        noticeList = new ArrayList<>();
        noticeAdapter = new NoticeAdapter(noticeList);
        recyclerViewNotices.setAdapter(noticeAdapter);

        fetchNotices();
    }

    private void fetchNotices() {
        db.collection("notices")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error fetching notices", e);
                        return;
                    }
                    if (queryDocumentSnapshots != null) {
                        noticeList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Notice notice = document.toObject(Notice.class);
                            if (notice.getDepartment().equals("All") || notice.getDepartment().equals(studentDepartment)) {
                                noticeList.add(notice);
                            }
                        }
                        noticeAdapter.notifyDataSetChanged();
                    }
                });
    }
}
