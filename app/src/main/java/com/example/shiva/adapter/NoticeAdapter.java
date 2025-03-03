package com.example.shiva.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.R;
import com.example.shiva.model.Notice;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private List<Notice> noticeList;
    private boolean isAdmin;  // 🔥 Admin hai toh true hoga, warna false
    private Context context;

    public NoticeAdapter(List<Notice> noticeList, boolean isAdmin) {
        this.noticeList = noticeList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_notice, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice notice = noticeList.get(position);
        holder.tvTitle.setText(notice.getTitle());
        holder.tvDescription.setText(notice.getDescription());
        holder.tvDepartment.setText("Department: " + notice.getDepartment());

        // 🔥 Delete button sirf admin ko dikhna chahiye
        if (isAdmin) {
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }

        // 🔥 Delete button functionality
        holder.btnDelete.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("notices")
                    .document(notice.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Notice Deleted!", Toast.LENGTH_SHORT).show();
                        noticeList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show()
                    );
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvDepartment;
        Button btnDelete;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_notice_title);
            tvDescription = itemView.findViewById(R.id.tv_notice_description);
            tvDepartment = itemView.findViewById(R.id.tv_notice_department);
            btnDelete = itemView.findViewById(R.id.btn_delete_notice);  // 🔥 Delete button
        }
    }
}
