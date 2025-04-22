package com.example.shiva;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.adapter.FeedbackAdapter;
import com.example.shiva.model.Feedback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Feedback_activity extends AppCompatActivity {
    private RecyclerView feedbackRecyclerView;
    private FeedbackAdapter feedbackAdapter;
    private List<Feedback> feedbackList;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView);
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        feedbackList = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(feedbackList);
        feedbackRecyclerView.setAdapter(feedbackAdapter);

        db = FirebaseFirestore.getInstance();

        fetchFeedback();
    }

    private void fetchFeedback() {
        db.collection("feedbacks")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    feedbackList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Feedback feedback = document.toObject(Feedback.class);
                        feedbackList.add(feedback);
                    }
                    feedbackAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Feedback_activity.this, "Error fetching feedback", Toast.LENGTH_SHORT).show();
                    Log.e("FeedbackActivity", "Error fetching feedback", e);
                });
    }
}
