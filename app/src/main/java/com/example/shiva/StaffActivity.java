package com.example.shiva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StaffActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffAdapter staffAdapter;
    private List<Staff> staffList;
    private DatabaseReference databaseRef;
    private Button btnAddStaff;
private Button FloatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton btnAddStaff = findViewById(R.id.btnAddStaff);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        staffList = new ArrayList<>();
        staffAdapter = new StaffAdapter(this, staffList);
        recyclerView.setAdapter(staffAdapter);

        databaseRef = FirebaseDatabase.getInstance().getReference("staff");

        // Load Data from Firebase
        loadStaffData();

        // Add Staff Button Click
        btnAddStaff.setOnClickListener(v -> {
            Intent intent = new Intent(StaffActivity.this, StaffAdapter.class);
            startActivity(intent);
        });
    }

    private void loadStaffData() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                staffList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Staff staff = dataSnapshot.getValue(Staff.class);
                    staffList.add(staff);
                }
                staffAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StaffActivity.this, "Failed to load data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
