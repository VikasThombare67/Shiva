package com.example.shiva;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.adapter.AdminNotesAdapter;
import com.example.shiva.model.NoteModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NotesUploadActivity extends AppCompatActivity {

    private AutoCompleteTextView departmentDropdown;
    private Button btnSelectFile, btnUpload;
    private TextView tvSelectedFile;
    private Uri fileUri;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private AdminNotesAdapter notesAdapter;
    private List<NoteModel> notesList;

    private FirebaseStorage storage;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_upload);

        departmentDropdown = findViewById(R.id.auto_complete_department);
        btnSelectFile = findViewById(R.id.btn_select_file);
        btnUpload = findViewById(R.id.btn_upload);
        tvSelectedFile = findViewById(R.id.tv_selected_file);
        recyclerView = findViewById(R.id.recycler_notes);
        progressDialog = new ProgressDialog(this);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        // Setup department dropdown
        String[] departments = {"Computer", "IT", "Civil", "Mechanical", "Electronics"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, departments);
        departmentDropdown.setAdapter(adapter);

        // Initialize RecyclerView
        notesList = new ArrayList<>();
        notesAdapter = new AdminNotesAdapter(notesList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);

        // Load Existing Notes
        fetchNotes();

        // File Picker
        ActivityResultLauncher<String> filePicker = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                fileUri = uri;
                tvSelectedFile.setText(getFileNameFromUri(fileUri));
            } else {
                tvSelectedFile.setText("No file selected");
            }
        });

        btnSelectFile.setOnClickListener(v -> filePicker.launch("application/pdf"));
        btnUpload.setOnClickListener(v -> uploadFile());
    }

    private void uploadFile() {
        if (fileUri == null) {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
            return;
        }
        if (departmentDropdown.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select a department", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        String department = departmentDropdown.getText().toString().trim();
        String originalFileName = getFileNameFromUri(fileUri);
        String uniqueFileName = UUID.randomUUID().toString();
        StorageReference fileRef = storage.getReference().child("notes/" + uniqueFileName);

        fileRef.putFile(fileUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            HashMap<String, Object> noteData = new HashMap<>();
            noteData.put("department", department);
            noteData.put("fileUrl", uri.toString());
            noteData.put("fileName", uniqueFileName);
            noteData.put("originalFileName", originalFileName);

            db.collection("notes").add(noteData).addOnSuccessListener(documentReference -> {
                progressDialog.dismiss();
                Toast.makeText(this, "File Uploaded!", Toast.LENGTH_SHORT).show();
                fileUri = null;
                tvSelectedFile.setText("No file selected");
                departmentDropdown.setText("");
                fetchNotes();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(this, "Upload Failed!", Toast.LENGTH_SHORT).show();
            });
        })).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, "File Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void fetchNotes() {
        // 🔥 Dynamic Department Fetch करणे (Firestore मधून)
        String studentDepartment = getStudentDepartmentFromFirestore();

        db.collection("notes")
                .whereEqualTo("department", studentDepartment) // 🔥 Student च्या विभागाचे Notes फक्त घेतो
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notesList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            NoteModel note = document.toObject(NoteModel.class);
                            if (note != null) {
                                note.setDocumentId(document.getId());
                            }
                            notesList.add(note);
                        }
                        notesAdapter.notifyDataSetChanged(); // 🔥 RecyclerView Update कर
                    } else {
                        Log.e("FETCH_NOTES", "Error getting notes", task.getException());
                    }
                });
    }


    private String getStudentDepartmentFromFirestore() {
        return "0"; //
    }
}