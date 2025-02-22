package com.example.shiva;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.adapter.NotesAdapter;
import com.example.shiva.model.Note;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotesViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private List<Note> notesList;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        recyclerView = findViewById(R.id.recycler_notes);
        progressBar = findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesList = new ArrayList<>();
        adapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fetchNotes();
    }

    private void fetchNotes() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("notes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notesList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Note note = document.toObject(Note.class);
                    notesList.add(note);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to load notes!", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}
