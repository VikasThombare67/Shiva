package com.example.shiva;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.shiva.adapter.NotesAdapter;
import com.example.shiva.model.NoteModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.material.textfield.TextInputLayout;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NotesUploadActivity extends AppCompatActivity {

    private TextInputLayout departmentLayout;
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

        departmentLayout = findViewById(R.id.text_input_layout_department);
        departmentDropdown = findViewById(R.id.auto_complete_department);
        btnSelectFile = findViewById(R.id.btn_select_file);
        btnUpload = findViewById(R.id.btn_upload);
        tvSelectedFile = findViewById(R.id.tv_selected_file);
        recyclerView = findViewById(R.id.recycler_notes);
        progressDialog = new ProgressDialog(this);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        // Setup department dropdown
        String[] departments = {"All Departments", "Computer", "IT", "Civil", "Mechanical", "Electronics"};
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
                tvSelectedFile.setText("Selected File: " + uri.getLastPathSegment());
            } else {
                tvSelectedFile.setText("No file selected");
            }
        });

        btnSelectFile.setOnClickListener(v -> filePicker.launch("*/*")); // Pick any file (PDF, Image, etc.)
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

        // Extract the original file name from URI
        String originalFileName = getFileNameFromUri(fileUri);
        String uniqueFileName = UUID.randomUUID().toString(); // Unique identifier
        StorageReference fileRef = storage.getReference().child("notes/" + uniqueFileName);

        fileRef.putFile(fileUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Store file details in Firestore
            HashMap<String, Object> noteData = new HashMap<>();
            noteData.put("department", department);
            noteData.put("fileUrl", uri.toString());
            noteData.put("fileName", uniqueFileName); // Store unique file name for Firebase Storage
            noteData.put("originalFileName", originalFileName); // Store actual file name for user reference

            db.collection("notes").add(noteData).addOnSuccessListener(documentReference -> {
                progressDialog.dismiss();
                Toast.makeText(this, "File Uploaded!", Toast.LENGTH_SHORT).show();
                fileUri = null;
                tvSelectedFile.setText("No file selected");
                departmentDropdown.setText("");
                fetchNotes(); // Refresh List
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(this, "Upload Failed!", Toast.LENGTH_SHORT).show();
            });
        })).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, "File Upload Failed!", Toast.LENGTH_SHORT).show();
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
        db.collection("notes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notesList.clear(); // List reset karo taaki purane data repeat na ho
                for (QueryDocumentSnapshot document : task.getResult()) {
                    NoteModel note = document.toObject(NoteModel.class);

                    // ✅ Sirf wahi notes dikhaye jo "All" ya student ke department ke hai
                    if (note.getDepartment().equals("All") || note.getDepartment().equals(notesList)) {
                        Log.d("FETCH_NOTES", "Note: " + note.getFileName());
                        notesList.add(note);
                    }
                }
                notesAdapter.notifyDataSetChanged(); // UI ko update karo
            }
        });
    }

}
