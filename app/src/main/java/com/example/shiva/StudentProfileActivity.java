package com.example.shiva;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class StudentProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private TextInputEditText etFullName, etFatherName, etMotherName, etDOB, etSSCPercentage, etHSCPercentage;
    private Spinner spinnerGender, spinnerCategory, spinnerReligion;
    private RadioGroup rgEducation;
    private LinearLayout layoutSSC, layoutHSC;
    private Button btnUploadCaste, btnUploadIncome, btnUploadNationality, btnUploadValidity, btnUploadSSC, btnUploadHSC, btnSubmit;
    private TextView tvCasteCertificate, tvIncomeCertificate, tvNationalityCertificate, tvCasteValidity, tvSSCMarksheet, tvHSCMarksheet;

    private static final int PICK_IMAGE = 1;
    private static final int PICK_PDF_CASTE = 2;
    private static final int PICK_PDF_INCOME = 3;
    private static final int PICK_PDF_NATIONALITY = 4;
    private static final int PICK_PDF_VALIDITY = 5;
    private static final int PICK_PDF_SSC = 6;
    private static final int PICK_PDF_HSC = 7;

    private Uri profileImageUri, castePdfUri, incomePdfUri, nationalityPdfUri, validityPdfUri, sscPdfUri, hscPdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);

        // Initialize UI elements
        imgProfile = findViewById(R.id.imgProfile);
        etFullName = findViewById(R.id.etFullName);
        etFatherName = findViewById(R.id.etFatherName);
        etMotherName = findViewById(R.id.etMotherName);
        etDOB = findViewById(R.id.etDOB);
        etSSCPercentage = findViewById(R.id.etSSCPercentage);
        etHSCPercentage = findViewById(R.id.etHSCPercentage);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerReligion = findViewById(R.id.spinnerReligion);
        rgEducation = findViewById(R.id.rgEducation);
        layoutSSC = findViewById(R.id.layoutSSC);
        layoutHSC = findViewById(R.id.layoutHSC);

        btnUploadCaste = findViewById(R.id.btnUploadCaste);
        btnUploadIncome = findViewById(R.id.btnUploadIncome);
        btnUploadNationality = findViewById(R.id.btnUploadNationality);
        btnUploadValidity = findViewById(R.id.btnUploadValidity);
        btnUploadSSC = findViewById(R.id.btnUploadSSC);
        btnUploadHSC = findViewById(R.id.btnUploadHSC);
        btnSubmit = findViewById(R.id.btnSubmit);

        tvCasteCertificate = findViewById(R.id.tvCasteCertificate);
        tvIncomeCertificate = findViewById(R.id.tvIncomeCertificate);
        tvNationalityCertificate = findViewById(R.id.tvNationalityCertificate);
        tvCasteValidity = findViewById(R.id.tvCasteValidity);
        tvSSCMarksheet = findViewById(R.id.tvSSCMarksheet);
        tvHSCMarksheet = findViewById(R.id.tvHSCMarksheet);

        // Profile image selection
        imgProfile.setOnClickListener(view -> openGallery());

        // Date picker for DOB
        etDOB.setOnClickListener(view -> showDatePicker());

        // Education selection logic
        rgEducation.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSSC) {
                layoutSSC.setVisibility(View.VISIBLE);
                layoutHSC.setVisibility(View.GONE);
            } else if (checkedId == R.id.rbHSC) {
                layoutSSC.setVisibility(View.GONE);
                layoutHSC.setVisibility(View.VISIBLE);
            }
        });

        // PDF Selection Handlers
        btnUploadSSC.setOnClickListener(view -> selectPdf(PICK_PDF_SSC));
        btnUploadHSC.setOnClickListener(view -> selectPdf(PICK_PDF_HSC));
        btnUploadCaste.setOnClickListener(view -> selectPdf(PICK_PDF_CASTE));
        btnUploadIncome.setOnClickListener(view -> selectPdf(PICK_PDF_INCOME));
        btnUploadNationality.setOnClickListener(view -> selectPdf(PICK_PDF_NATIONALITY));
        btnUploadValidity.setOnClickListener(view -> selectPdf(PICK_PDF_VALIDITY));

        // Submit Button Click
        btnSubmit.setOnClickListener(view -> submitForm());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> etDOB.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    private void selectPdf(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedUri = data.getData();

            if (requestCode == PICK_IMAGE) {
                // ✅ Set selected image to ImageView
                profileImageUri = selectedUri;
                imgProfile.setImageURI(profileImageUri);
            } else if (requestCode == PICK_PDF_SSC) {
                sscPdfUri = selectedUri;
                tvSSCMarksheet.setText("SSC Marksheet Selected");
            } else if (requestCode == PICK_PDF_HSC) {
                hscPdfUri = selectedUri;
                tvHSCMarksheet.setText("HSC Marksheet Selected");
            } else if (requestCode == PICK_PDF_CASTE) {
                castePdfUri = selectedUri;
                tvCasteCertificate.setText("Caste Certificate Selected");
            } else if (requestCode == PICK_PDF_INCOME) {
                incomePdfUri = selectedUri;
                tvIncomeCertificate.setText("Income Certificate Selected");
            } else if (requestCode == PICK_PDF_NATIONALITY) {
                nationalityPdfUri = selectedUri;
                tvNationalityCertificate.setText("Nationality Certificate Selected");
            } else if (requestCode == PICK_PDF_VALIDITY) {
                validityPdfUri = selectedUri;
                tvCasteValidity.setText("Caste Validity Selected");
            }
        }
    }

    private void submitForm() {
        String fullName = etFullName.getText().toString().trim();
        String dob = etDOB.getText().toString().trim();

        if (fullName.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Form Submitted Successfully", Toast.LENGTH_SHORT).show();
    }
}
