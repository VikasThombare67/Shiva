package com.example.shiva;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.adapter.NoticeAdapter;
import com.example.shiva.model.Notice;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminNoticeActivity extends AppCompatActivity {

    private EditText etNoticeTitle, etNoticeDescription;
    private Spinner spinnerDepartment;
    private Button btnPostNotice;
    private RecyclerView recyclerViewNotices;
    private NoticeAdapter noticeAdapter;
    private List<Notice> noticeList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice);

        db = FirebaseFirestore.getInstance();

        etNoticeTitle = findViewById(R.id.et_notice_title);
        etNoticeDescription = findViewById(R.id.et_notice_description);
        spinnerDepartment = findViewById(R.id.spinner_department);
        btnPostNotice = findViewById(R.id.btn_post_notice);
        recyclerViewNotices = findViewById(R.id.recyclerViewNotices);

        // Setup RecyclerView
        recyclerViewNotices.setLayoutManager(new LinearLayoutManager(this));
        noticeList = new ArrayList<>();
        noticeAdapter = new NoticeAdapter(noticeList, true);  // 🔥 Admin hai toh 'true' pass karein (Delete button dikhaye)
        recyclerViewNotices.setAdapter(noticeAdapter);

        // Populate Spinner with Departments and "All"
        List<String> departments = Arrays.asList("All", "Computer", "IT", "Civil", "Mechanical", "Electronics");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(adapter);

        // Button Click Listener
        btnPostNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNotice();
            }
        });

        // Fetch and display notices
        fetchNotices();
    }

    private void postNotice() {
        String title = etNoticeTitle.getText().toString().trim();
        String description = etNoticeDescription.getText().toString().trim();
        String department = spinnerDepartment.getSelectedItem().toString();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Auto-generate Notice ID
        DocumentReference noticeRef = db.collection("notices").document();
        String noticeId = noticeRef.getId();

        Notice notice = new Notice(noticeId, title, description, department);

        // Store in Firestore
        noticeRef.set(notice).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("NOTICE_UPLOAD", "Notice Posted Successfully: " + noticeId);
                Toast.makeText(AdminNoticeActivity.this, "Notice Posted Successfully", Toast.LENGTH_SHORT).show();
                etNoticeTitle.setText("");
                etNoticeDescription.setText("");
                fetchNotices();  // Refresh notices list
            } else {
                Log.e("NOTICE_UPLOAD", "Error Posting Notice: ", task.getException());
                Toast.makeText(AdminNoticeActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchNotices() {
        db.collection("notices").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                noticeList.clear();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    Notice notice = document.toObject(Notice.class);
                    noticeList.add(notice);
                }
                noticeAdapter.notifyDataSetChanged();
            } else {
                Log.e("FETCH_NOTICES", "Error getting notices", task.getException());
            }
        });
    }
}
