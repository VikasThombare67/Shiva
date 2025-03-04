package com.example.shiva.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shiva.R;
import com.example.shiva.model.Stafff;

import java.util.ArrayList;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {
    private Context context;
    private ArrayList<Stafff> staffList;

    public StaffAdapter(Context context, ArrayList<Stafff> staffList) {
        this.context = context;
        this.staffList = staffList;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_staff, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        Stafff staff = staffList.get(position);
        holder.tvStaffName.setText(staff.getName());
        holder.tvStaffDepartment.setText(staff.getDepartment());
        holder.tvStaffContact.setText(staff.getContact());
        Glide.with(context).load(staff.getImageUrl()).placeholder(R.drawable.iconsplaceholder).into(holder.ivStaffImage);
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView tvStaffName, tvStaffDepartment, tvStaffContact;
        ImageView ivStaffImage;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStaffName = itemView.findViewById(R.id.tv_staff_name);
            tvStaffDepartment = itemView.findViewById(R.id.tv_staff_department);
            tvStaffContact = itemView.findViewById(R.id.tv_staff_contact);
            ivStaffImage = itemView.findViewById(R.id.iv_staff_image);
        }
    }
}
