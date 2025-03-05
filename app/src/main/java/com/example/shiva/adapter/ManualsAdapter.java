package com.example.shiva.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.R;
import com.example.shiva.model.Manual;

import java.util.List;

public class ManualsAdapter extends RecyclerView.Adapter<ManualsAdapter.ManualViewHolder> {

    private Context context;
    private List<Manual> manualList;

    public ManualsAdapter(Context context, List<Manual> manualList) {
        this.context = context;
        this.manualList = manualList;
    }

    // ➡️⬇️  setManualList() Method टाकला आहे
    public void setManualList(List<Manual> manualList) {
        this.manualList = manualList;
        notifyDataSetChanged();  // UI अपडेट करण्यासाठी
    }

    @NonNull
    @Override
    public ManualViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manual, parent, false);
        return new ManualViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManualViewHolder holder, int position) {
        Manual manual = manualList.get(position);
        holder.tvTitle.setText(manual.getTitle());
        holder.tvDepartment.setText("Department: " + manual.getDepartment());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(manual.getPdfUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return manualList.size();
    }

    public static class ManualViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDepartment;

        public ManualViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvManualTitle);
            tvDepartment = itemView.findViewById(R.id.tvManualDepartment);
        }
    }
}
