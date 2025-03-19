package com.example.shiva.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shiva.model.StudentProfileModel;
import com.example.shiva.R;
import java.util.List;

public class StudentProfileAdapter extends RecyclerView.Adapter<StudentProfileAdapter.StudentViewHolder> {
    private List<StudentProfileModel> studentList;

    public StudentProfileAdapter(List<StudentProfileModel> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentProfileModel student = studentList.get(position);
        holder.txtFullName.setText("Name: " + student.getFullName());
        holder.txtEmail.setText("Email: " + student.getEmail());
        holder.txtMobile.setText("Mobile: " + student.getMobile());
        holder.txtBranch.setText("Branch: " + student.getBranch());
        holder.txtDob.setText("DOB: " + student.getDob());
        holder.txtAadhaar.setText("Aadhaar: " + student.getAadhaar());
        holder.txtBloodGroup.setText("Blood Group: " + student.getBloodGroup());
        holder.txtCaste.setText("Caste: " + student.getCaste());
        holder.txtCategory.setText("Category: " + student.getCategory());
        holder.txtCity.setText("City: " + student.getCity());
        holder.txtGender.setText("Gender: " + student.getGender());
        holder.txtNationality.setText("Nationality: " + student.getNationality());
        holder.txtPermanentAddress.setText("Address: " + student.getPermanentAddress());
        holder.txtPinCode.setText("Pin Code: " + student.getPinCode());
        holder.txtReligion.setText("Religion: " + student.getReligion());
        holder.txtState.setText("State: " + student.getState());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView txtFullName, txtEmail, txtMobile, txtBranch, txtDob, txtAadhaar, txtBloodGroup, txtCaste, txtCategory, txtCity, txtGender, txtNationality, txtPermanentAddress, txtPinCode, txtReligion, txtState;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFullName = itemView.findViewById(R.id.txtFullName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtBranch = itemView.findViewById(R.id.txtBranch);
            txtDob = itemView.findViewById(R.id.txtDob);
            txtAadhaar = itemView.findViewById(R.id.txtAadhaar);
            txtBloodGroup = itemView.findViewById(R.id.txtBloodGroup);
            txtCaste = itemView.findViewById(R.id.txtCaste);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtCity = itemView.findViewById(R.id.txtCity);
            txtGender = itemView.findViewById(R.id.txtGender);
            txtNationality = itemView.findViewById(R.id.txtNationality);
            txtPermanentAddress = itemView.findViewById(R.id.txtPermanentAddress);
            txtPinCode = itemView.findViewById(R.id.txtPinCode);
            txtReligion = itemView.findViewById(R.id.txtReligion);
            txtState = itemView.findViewById(R.id.txtState);
        }
    }
}
