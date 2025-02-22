package com.example.shiva;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.UUID;

public class NotesUploadActivity extends AppCompatActivity {

    private Spinner departmentSpinner;
    private Button btnSelectFile, btnUpload;
    private Uri fileUri;
    private ProgressDialog progressDialog;

    private FirebaseStorage storage;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_upload);

        departmentSpinner = findViewById(R.id.spinner_department);
        btnSelectFile = findViewById(R.id.btn_select_file);
        btnUpload = findViewById(R.id.btn_upload);
        progressDialog = new ProgressDialog(this);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        // Setup department spinner
        String[] departments = {"Computer", "IT", "Civil", "Mechanical", "Electronics"};
        departmentSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, departments));

        // File Picker
        ActivityResultLauncher<String> filePicker = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                fileUri = uri;
                Toast.makeText(this, "File Selected!", Toast.LENGTH_SHORT).show();
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

        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        String department = departmentSpinner.getSelectedItem().toString();
        String fileName = UUID.randomUUID().toString(); // Generate a unique file name
        StorageReference fileRef = storage.getReference().child("notes/" + fileName);

        fileRef.putFile(fileUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Store file URL in Firestore
            HashMap<String, Object> noteData = new HashMap<>();
            noteData.put("department", department);
            noteData.put("fileUrl", uri.toString());

            db.collection("notes").add(noteData).addOnSuccessListener(documentReference -> {
                progressDialog.dismiss();
                Toast.makeText(this, "File Uploaded!", Toast.LENGTH_SHORT).show();
                fileUri = null; // Reset selection
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(this, "Upload Failed!", Toast.LENGTH_SHORT).show();
            });
        })).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, "File Upload Failed!", Toast.LENGTH_SHORT).show();
        });
    }
}
