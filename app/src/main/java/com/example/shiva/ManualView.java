package com.example.shiva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.adapter.ManualsAdapter;
import com.example.shiva.model.Manual;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManualView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManualsAdapter adapter;
    private List<Manual> manualList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manualsview);

        recyclerView = findViewById(R.id.recyclerViewManuals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        manualList = new ArrayList<>();
        adapter = new ManualsAdapter(this, manualList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        fetchManuals();
    }
    private void getManuals() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String studentDepartment = prefs.getString("student_department", "");

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Manuals")
                .whereEqualTo("department", studentDepartment)  // फक्त त्या विभागाचे मॅन्युअल्स मिळवा
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Manual> manualList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Manual manual = doc.toObject(Manual.class);
                        manualList.add(manual);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load manuals!", Toast.LENGTH_SHORT).show();
                });
    }


    private void fetchManuals() {
        firestore.collection("Manuals").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DocumentSnapshot doc : task.getResult()) {
                    Manual manual = doc.toObject(Manual.class);
                    if (manual != null) {
                        manualList.add(manual);
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to fetch manuals!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
