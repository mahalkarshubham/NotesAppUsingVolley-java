package com.shubham.mahalkar.notesappusingvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by shubham on 23,May,2020
 */
public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.ViewHolder> {

    private List<ModelNotes> NoteModel;
    private Context context;

    public AdapterNotes(List<ModelNotes> ModelNotes, Context context) {
        this.NoteModel = ModelNotes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelNotes notes = NoteModel.get(position);

        holder.tvNotesId.setText(notes.getNotesId());
        holder.tvNotesTitle.setText(notes.getNotesTitle());
        holder.tvNotesDescription.setText(notes.getNotesDescription());
    }

    @Override
    public int getItemCount() {
        return NoteModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotesId, tvNotesTitle, tvNotesDescription;

        ViewHolder(View v) {
            super(v);
            tvNotesId = v.findViewById(R.id.tvNotesId);
            tvNotesTitle = v.findViewById(R.id.tvNotesTitle);
            tvNotesDescription = v.findViewById(R.id.tvNotesDescription);
        }
    }
}
