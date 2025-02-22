package com.example.shiva;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shiva.model.Notice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class AdminNoticeActivity extends AppCompatActivity {

    private EditText etNoticeTitle, etNoticeDescription;
    private Spinner spinnerDepartment;
    private Button btnPostNotice;
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
        noticeRef.set(notice).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminNoticeActivity.this, "Notice Posted Successfully", Toast.LENGTH_SHORT).show();
                    etNoticeTitle.setText("");
                    etNoticeDescription.setText("");
                } else {
                    Toast.makeText(AdminNoticeActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
