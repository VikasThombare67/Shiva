package com.example.shiva;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.adapter.ManualsAdapter;
import com.example.shiva.model.Manual;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        getManuals();  // 🔄 हे Method कॉल कर
    }

    // 🔄 विभागानुसार Manuals मिळवण्यासाठी Method
    private void getManuals() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);  // 🔄 इथे MyPrefs ऐवजी MyAppPrefs वापर!
        String studentDepartment = prefs.getString("student_department", "");  // 🔄 Key बदलली: student_department

        if (studentDepartment.isEmpty()) {
            Toast.makeText(this, "Department information missing!", Toast.LENGTH_SHORT).show();  // 🔄 Toast बदलला
            return;
        }

        firestore.collection("Manuals")
                .whereEqualTo("department", studentDepartment)
                .addSnapshotListener((queryDocumentSnapshots, error) -> {  // 🔄 लाईव्ह अपडेट्स मिळवा
                    if (error != null) {
                        Toast.makeText(this, "Failed to load manuals!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        manualList.clear();  // 🔄 जुनी लिस्ट क्लिअर करा
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Manual manual = doc.toObject(Manual.class);
                            if (manual != null) {
                                manualList.add(manual);
                            }
                        }
                        adapter.notifyDataSetChanged();  // 🔄 UI अपडेट करा
                    } else {
                        Toast.makeText(this, "No manuals available!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}