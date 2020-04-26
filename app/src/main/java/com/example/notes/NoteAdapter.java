package com.example.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.database.model.Note;
import com.example.notes.databinding.NoteRowBinding;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        NoteRowBinding itemBinding = NoteRowBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.binding.textViewTitle.setText(currentNote.getTitle());
        holder.binding.textViewDescription.setText(currentNote.getDescription());

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        NoteRowBinding binding;

        NoteHolder(NoteRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

            binding.deleteNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onRemoveClick(notes.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onRemoveClick(Note note);
    }

    public void itemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}