package com.example.shiva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private Context context;
    private List<Staff> staffList;

    // Constructor
    public StaffAdapter(Context context, List<Staff> staffList) {
        this.context = context;
        this.staffList = staffList;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_profile, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        Staff staff = staffList.get(position);

        // Set Data to Views
        holder.tvName.setText(staff.getName());
        holder.tvBranch.setText(staff.getBranch());
        holder.tvContact.setText(staff.getContact());

        // Load Image using Glide
        Glide.with(context)
                .load(staff.getImageUrl())
                .placeholder(R.drawable.ic_profile) // Default image
                .into(holder.imgProfile);
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView tvName, tvBranch, tvContact;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvBranch = itemView.findViewById(R.id.tvBranch);
            tvContact = itemView.findViewById(R.id.tvContact);
        }
    }
}
