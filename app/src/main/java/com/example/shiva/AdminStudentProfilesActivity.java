package com.example.shiva;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.adapter.StudentProfileAdapter;
import com.example.shiva.model.StudentProfileModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminStudentProfilesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentProfileAdapter adapter;
    private List<StudentProfileModel> studentList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_details);

        // 🔹 UI Components Initialize
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 🔹 List आणि Adapter Initialize
        studentList = new ArrayList<>();
        adapter = new StudentProfileAdapter(studentList);
        recyclerView.setAdapter(adapter);

        // 🔹 Firestore Instance
        db = FirebaseFirestore.getInstance();

        // 🔹 Data Fetch Method Call
        fetchAllStudents();
    }

    private void fetchAllStudents() {
        db.collection("student_profiles")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        studentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            StudentProfileModel student = document.toObject(StudentProfileModel.class);
                            studentList.add(student);
                        }
                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            Log.d("FirestoreDebug", "✔️ Adapter notified with " + studentList.size() + " students.");
                        });
                    } else {
                        Toast.makeText(this, "❌ डेटा आणता आला नाही!", Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreDebug", "❌ Error fetching student data", task.getException());
                    }
                });
    }
}