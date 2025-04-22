package com.example.shiva.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.R;
import com.example.shiva.model.Feedback;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    private List<Feedback> feedbackList;

    public FeedbackAdapter(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_feedback_item, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        holder.userNameTextView.setText(feedback.getUserName());
        holder.messageTextView.setText(feedback.getMessage());

        // Timestamp formatting
        Timestamp timestamp = feedback.getTimestamp();
        if (timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            holder.timestampTextView.setText(sdf.format(timestamp.toDate()));
        } else {
            holder.timestampTextView.setText("No timestamp");
        }

        // Set type
        holder.typeTextView.setText(feedback.getType());

        // Set rating value to RatingBar
        holder.ratingBar.setRating(feedback.getRating());  // Bind rating to the RatingBar
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView messageTextView;
        TextView typeTextView;
        TextView timestampTextView;
        RatingBar ratingBar;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            ratingBar = itemView.findViewById(R.id.ratingBarFeedback);  // Initialize RatingBar
        }
    }
}
