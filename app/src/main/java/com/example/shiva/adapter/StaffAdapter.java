package com.example.shiva.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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

        // ✅ Null Check for Staff Data
        if (staff != null) {
            holder.tvName.setText(staff.getName() != null ? staff.getName() : "N/A");
            holder.tvDepartment.setText(staff.getDepartment() != null ? staff.getDepartment() : "N/A");
            holder.tvContact.setText(staff.getContact() != null ? staff.getContact() : "N/A");

            // ✅ Load image using Glide with error placeholder
            Glide.with(context)
                    .load(staff.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.iconsplaceholder)  // Placeholder while loading
                            .error(R.drawable.error)              // Error image if loading fails
                            .diskCacheStrategy(DiskCacheStrategy.ALL))  // Cache images for better performance
                    .into(holder.ivStaffImage);
        } else {
            Toast.makeText(context, "Staff data is missing!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return staffList != null ? staffList.size() : 0;  // ✅ Null Check for staffList
    }

    // 🔄 Optional: Method to update data dynamically
    public void updateStaffList(ArrayList<Stafff> newStaffList) {
        staffList = newStaffList;
        notifyDataSetChanged();
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDepartment, tvContact;
        ImageView ivStaffImage;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_staff_name);
            tvDepartment = itemView.findViewById(R.id.tv_staff_department);
            tvContact = itemView.findViewById(R.id.tv_staff_contact);
            ivStaffImage = itemView.findViewById(R.id.iv_staff_image);
        }
    }
}
