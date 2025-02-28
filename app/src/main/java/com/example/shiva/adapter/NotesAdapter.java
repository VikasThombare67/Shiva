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
import com.example.shiva.model.Note;
import com.example.shiva.model.NoteModel;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<NoteModel> notesList;
    private Context context;


    public NotesAdapter(List<NoteModel> notesList, Context context) {
        this.notesList = notesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteModel note = notesList.get(position);
        holder.tvDepartment.setText("Department: " + note.getDepartment());
        holder.tvFileUrl.setText("View File");
        holder.tvfilename.setText(note.getFileName());

        holder.tvFileUrl.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(note.getFileUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepartment, tvFileUrl;
        TextView tvfilename;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDepartment = itemView.findViewById(R.id.tv_department);
            tvFileUrl = itemView.findViewById(R.id.tv_file_url);
            tvfilename= itemView.findViewById(R.id.tvfilename);
        }
    }
}
