package com.example.shiva;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.adapter.FeedbackAdapter;
import com.example.shiva.model.Feedback;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminFeedbackActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FeedbackAdapter adapter;
    private List<Feedback> feedbackList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback);

        recyclerView = findViewById(R.id.feedbackRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        feedbackList = new ArrayList<>();
        adapter = new FeedbackAdapter(feedbackList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("feedbacks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(AdminFeedbackActivity.this, "Error fetching data!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        feedbackList.clear();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Feedback feedback = doc.toObject(Feedback.class);
                            feedbackList.add(feedback);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
