package com.example.shiva;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManualUpload extends AppCompatActivity {

    private static final int PDF_PICK_CODE = 1000;
    private EditText etManualTitle;
    private Spinner spinnerDepartment;
    private Button btnSelectPdf, btnUploadPdf;
    private ProgressBar progressBar;

    private Uri pdfUri;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuals);

        // 🔄 Initialize Views
        etManualTitle = findViewById(R.id.etManualTitle);
        spinnerDepartment = findViewById(R.id.spinnerDepartment);
        btnSelectPdf = findViewById(R.id.btnSelectPdf);
        btnUploadPdf = findViewById(R.id.btnUploadPdf);
        progressBar = findViewById(R.id.progressBar);

        // 🔄 Firebase Instances
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btnSelectPdf.setOnClickListener(v -> pickPdf());
        btnUploadPdf.setOnClickListener(v -> uploadManual());
    }

    // 🔄 PDF Picker Method
    private void pickPdf() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PDF_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            pdfUri = data.getData();
            Toast.makeText(this, "PDF Selected", Toast.LENGTH_SHORT).show();
        }
    }

    // 🔄 Upload Manual Method
    private void uploadManual() {
        String title = etManualTitle.getText().toString().trim();
        String department = spinnerDepartment.getSelectedItem().toString();

        // 🔄 Validation Checks
        if (TextUtils.isEmpty(title)) {
            etManualTitle.setError("Title is required!");
            return;
        }
        if (pdfUri == null) {
            Toast.makeText(this, "Please select a PDF!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (department.equals("Select Department")) {
            Toast.makeText(this, "Please select a department!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // 🔄 PDF Upload
        String manualId = UUID.randomUUID().toString();
        StorageReference ref = storage.getReference().child("manuals/" + manualId + ".pdf");

        ref.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    saveManualInfo(uri.toString(), title, department);
                }))
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Upload Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // 🔄 Save Manual Info in Firestore
    private void saveManualInfo(String pdfUrl, String title, String department) {
        Map<String, Object> manual = new HashMap<>();
        manual.put("title", title);
        manual.put("department", department);
        manual.put("pdfUrl", pdfUrl);

        firestore.collection("Manuals")
                .add(manual)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Manual Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    etManualTitle.setText("");  // Clear input
                    pdfUri = null;  // Clear selected PDF
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to Save Manual Info! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
