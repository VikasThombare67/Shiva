package com.example.shiva;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.shiva.adapter.NoticeAdapter;
import com.example.shiva.model.Notice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.Arrays;
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
        noticeAdapter = new NoticeAdapter(noticeList, false);
        recyclerViewNotices.setAdapter(noticeAdapter);

        fetchNotices();
    }

    private void fetchNotices() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "❌ Please log in first!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("students").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String studentDepartment = documentSnapshot.getString("department");

                db.collection("notices")
                        .whereIn("department", Arrays.asList(studentDepartment, "All")) // 🔥 Student च्या विभागाचे आणि "All" Notices घ्या!
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                noticeList.clear();
                                for (DocumentSnapshot document : task.getResult()) {
                                    Notice notice = document.toObject(Notice.class);
                                    noticeList.add(notice);
                                }
                                noticeAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("FETCH_NOTICES", "❌ Error getting notices", task.getException());
                            }
                        });
            }
        });
    }
}
