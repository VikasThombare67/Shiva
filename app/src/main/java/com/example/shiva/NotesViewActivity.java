package com.example.shiva;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.adapter.NotesAdapter;
import com.example.shiva.model.Note;
import com.example.shiva.model.NoteModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotesViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private List<NoteModel> notesList;
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "❌ Please log in first!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("students").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String studentDepartment = documentSnapshot.getString("department");

                db.collection("notes")
                        .whereEqualTo("department", studentDepartment) // 🔥 फक्त Student च्या विभागाचे Notes घ्या
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                notesList.clear();
                                for (DocumentSnapshot document : task.getResult()) {
                                    NoteModel note = document.toObject(NoteModel.class);
                                    if (note != null) {
                                        note.setDocumentId(document.getId());
                                        note.setFileName(document.getString("originalFileName")); // ✅ Get Original PDF Name
                                    }
                                    notesList.add(note);
                                }
                                adapter.notifyDataSetChanged(); // UI Update
                            } else {
                                Log.e("FETCH_NOTES", "❌ Error getting notes", task.getException());
                            }
                        });
            }
        });
    }
}
