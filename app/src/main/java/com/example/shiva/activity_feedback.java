package com.example.shiva;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class activity_feedback extends AppCompatActivity {

    private Spinner spinnerFeedbackType;
    private EditText editTextFeedbackMessage;
    private Button buttonSubmitFeedback;
    private RatingBar ratingBar;  // Added RatingBar reference

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        spinnerFeedbackType = findViewById(R.id.spinner_feedback_type);
        editTextFeedbackMessage = findViewById(R.id.edittext_feedback_message);
        buttonSubmitFeedback = findViewById(R.id.button_submit_feedback);
        ratingBar = findViewById(R.id.rating_bar);  // Initialize RatingBar

        // Set click listener for submit button
        buttonSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        String feedbackType = spinnerFeedbackType.getSelectedItem().toString();
        String feedbackMessage = editTextFeedbackMessage.getText().toString().trim();
        float rating = ratingBar.getRating();  // Get the rating from RatingBar

        if (feedbackMessage.isEmpty()) {
            editTextFeedbackMessage.setError("Please enter your feedback");
            editTextFeedbackMessage.requestFocus();
            return;
        }

        // Create a feedback object
        Map<String, Object> feedback = new HashMap<>();
        feedback.put("type", feedbackType);
        feedback.put("message", feedbackMessage);
        feedback.put("rating", rating);  // Add rating to feedback
        feedback.put("timestamp", Timestamp.now());

        // Add feedback to Firestore
        db.collection("feedbacks")
                .add(feedback)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(activity_feedback.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                    // Clear the input fields
                    editTextFeedbackMessage.setText("");
                    spinnerFeedbackType.setSelection(0);
                    ratingBar.setRating(0);  // Reset rating after submission
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity_feedback.this, "Error submitting feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
