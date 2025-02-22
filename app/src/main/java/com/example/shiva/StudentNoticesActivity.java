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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentNoticesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotices;
    private NoticeAdapter noticeAdapter;
    private List<Notice> noticeList;
    private FirebaseFirestore db;
    private String studentId, studentEmail, studentDepartment;
    private static final String TAG = "StudentDashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notices);

        SharedPreferences sharedPreferences = getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);
        studentId = sharedPreferences.getString("studentId", "N/A");
        studentEmail = sharedPreferences.getString("email", "N/A");
        studentDepartment = sharedPreferences.getString("department", "All");

        Log.d(TAG, "Student ID: " + studentId);
        Log.d(TAG, "Student Email: " + studentEmail);
        Log.d(TAG, "Student Department: " + studentDepartment);

        recyclerViewNotices = findViewById(R.id.recyclerViewNotices);
        recyclerViewNotices.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        noticeList = new ArrayList<>();
        noticeAdapter = new NoticeAdapter(noticeList);
        recyclerViewNotices.setAdapter(noticeAdapter);

        fetchNotices();
    }

    private void fetchNotices() {
        db.collection("notices").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Notice notice = document.toObject(Notice.class);
                    if (notice.getDepartment().equals("All") || notice.getDepartment().equals(studentDepartment)) {
                        Log.d(TAG, "Notice: " + notice.getTitle());
                        noticeList.add(notice);
                    }
                }
                noticeAdapter.notifyDataSetChanged();
            }
        });
    }
}
