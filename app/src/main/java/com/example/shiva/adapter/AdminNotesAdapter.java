package com.example.shiva.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiva.R;
import com.example.shiva.model.NoteModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdminNotesAdapter extends RecyclerView.Adapter<AdminNotesAdapter.ViewHolder> {
    private List<NoteModel> notesList;
    private Context context;

    public AdminNotesAdapter(List<NoteModel> notesList, Context context) {
        this.notesList = notesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_admin, parent, false);
        return new ViewHolder(view);
    }
/*
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteModel note = notesList.get(position);
        holder.tvFileName.setText(note.getDepartment() + " - File");

        holder.btnView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(note.getFileUrl()));
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference fileRef = storage.getReference().child("notes/" + note.getFileName());
            fileRef.delete().addOnSuccessListener(aVoid -> {
                db.collection("notes").document(note.getDocumentId()).delete().addOnSuccessListener(aVoid1 -> {
                    notesList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> Toast.makeText(context, "Failed to delete file!", Toast.LENGTH_SHORT).show());
        });
    }*/

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    NoteModel note = notesList.get(position);
    holder.tvFileName.setText(note.getDepartment() + " - File");

    holder.btnView.setOnClickListener(v -> {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(note.getFileUrl()));
        context.startActivity(intent);
    });

    holder.tvFileName.setText(note.getOriginalFileName() + " (" + note.getDepartment() + ")");
    holder.btnDelete.setOnClickListener(v -> {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Ensure fileName is retrieved correctly
        if (note.getFileName() == null || note.getFileName().isEmpty()) {
            Toast.makeText(context, "File name missing! Cannot delete.", Toast.LENGTH_SHORT).show();
            return;
        }



        StorageReference fileRef = storage.getReference().child("notes/" + note.getFileName());
        fileRef.delete().addOnSuccessListener(aVoid -> {
            db.collection("notes").document(note.getDocumentId()).delete().addOnSuccessListener(aVoid1 -> {
                notesList.remove(position);
                notifyItemRemoved(position); // More efficient than notifyDataSetChanged()
                Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(context, "Failed to delete document!", Toast.LENGTH_SHORT).show());
        }).addOnFailureListener(e -> Toast.makeText(context, "Failed to delete file!", Toast.LENGTH_SHORT).show());
    });
}


    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        Button btnView, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
            btnView = itemView.findViewById(R.id.btn_view);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}

