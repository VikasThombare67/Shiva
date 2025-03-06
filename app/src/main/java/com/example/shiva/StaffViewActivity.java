package com.example.shiva;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.adapter.StaffAdapter;
import com.example.shiva.model.Stafff;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StaffViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffAdapter staffAdapter;
    private ArrayList<Stafff> staffList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_view);

        // 🔄 Initialize Views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        // 🔄 RecyclerView Setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        staffList = new ArrayList<>();
        staffAdapter = new StaffAdapter(this, staffList);
        recyclerView.setAdapter(staffAdapter);

        // 🔄 Fetch Data from Firebase
        fetchStaffData();
    }

    // 🔄 Proper `fetchStaffData()` method with SharedPreferences handling
    private void fetchStaffData() {
        progressBar.setVisibility(View.VISIBLE);

        // 🔄 SharedPreferences मधून student's department मिळव
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String studentDept = preferences.getString("student_department", "");  // 🔄 इथे Key बदलला!

        Log.d("DeptCheck", "Student Department: " + studentDept);  // 🔄 Log Check

        if (!studentDept.isEmpty()) {  // 🔄 department data असेल तरच fetch कर
            DatabaseReference staffRef = FirebaseDatabase.getInstance().getReference("Staff").child(studentDept);

            staffRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        staffList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Stafff staff = dataSnapshot.getValue(Stafff.class);
                            if (staff != null) {
                                Log.d("StaffData", "Name: " + staff.getName() + ", Department: " + staff.getDepartment());
                                staffList.add(staff);
                            }
                        }
                        staffAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(StaffViewActivity.this, "No staff found for your department!", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", error.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Department information missing!", Toast.LENGTH_SHORT).show();
        }
    }
}