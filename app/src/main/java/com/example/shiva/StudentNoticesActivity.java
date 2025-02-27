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
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
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
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error Fetching Notices: ", error);
                        return;
                    }

                    if (value != null) {
                        noticeList.clear();
                        boolean noticeFound = false;
                        for (QueryDocumentSnapshot document : value) {
                            Notice notice = document.toObject(Notice.class);
                            Log.d(TAG, "Fetched Notice: " + notice.getTitle() + ", Dept: " + notice.getDepartment());

                            // Ensure notice is added correctly
                            if ("All".equals(notice.getDepartment()) || notice.getDepartment().equals(studentDepartment)) {
                                noticeList.add(notice);
                                noticeFound = true;
                            }
                        }

                        if (!noticeFound) {
                            Log.d(TAG, "No notices found for department: " + studentDepartment);
                        }

                        // Ensure UI updates on main thread
                        runOnUiThread(() -> noticeAdapter.notifyDataSetChanged());
                    }
                });
    }
}
