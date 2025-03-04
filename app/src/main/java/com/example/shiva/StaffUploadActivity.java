package com.example.shiva;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shiva.model.Stafff;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StaffUploadActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private EditText etName, etContact;
    private Spinner spBranch;
    private Button btnSelectImage, btnUpload;
    private Uri imageUri;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        ivProfile = findViewById(R.id.iv_profile);
        etName = findViewById(R.id.et_name);
        etContact = findViewById(R.id.et_contact);
        spBranch = findViewById(R.id.sp_branch);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnUpload = findViewById(R.id.btn_upload1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBranch.setAdapter(adapter);

        databaseRef = FirebaseDatabase.getInstance().getReference("Staff");
        storageRef = FirebaseStorage.getInstance().getReference("staff_images");

        btnSelectImage.setOnClickListener(v -> selectImage());
        btnUpload.setOnClickListener(v -> uploadStaffData());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivProfile.setImageURI(imageUri);
        }
    }

    private void uploadStaffData() {
        String name = etName.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String branch = spBranch.getSelectedItem().toString();

        // ✅ Input Validation
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(contact) || imageUri == null) {
            Toast.makeText(this, "All fields and image selection are required!", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String fileName = System.currentTimeMillis() + ".jpg";
        StorageReference imgRef = storageRef.child(fileName);

        imgRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    String staffId = databaseRef.push().getKey();
                    Stafff staff = new Stafff(staffId, name, branch, contact, imageUrl);

                    databaseRef.child(branch).child(staffId).setValue(staff)
                            .addOnSuccessListener(unused -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Staff uploaded successfully!", Toast.LENGTH_SHORT).show();
                                finish();  // Activity बंद करून मागे जा
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Failed to upload staff: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });

                })).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
