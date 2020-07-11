package com.example.notes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.database.model.Note;
import com.example.notes.databinding.MainFragmentBinding;
import com.example.notes.ui.NoteAdapter;
import com.example.notes.ui.SharedViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {
    private MainFragmentBinding binding;
    private SharedViewModel sharedViewModel;
    private NoteAdapter noteRecAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecyclerView();
        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        sharedViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> noteRecAdapter.submitList(notes));
        noteRecAdapter.itemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(final Note note) {
                deleteAlertDialog(note);
            }

            @Override
            public void onRootClick(Note note) {
                sharedViewModel.setNote(note);
                attachNoteUpdateFragment();
            }
        });

        //swipe to delete note
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                sharedViewModel.delete(noteRecAdapter.getNoteAtIndex(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(binding.recId);

        binding.addNote.setOnClickListener(v -> attachAddNoteFragment());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initRecyclerView() {
        binding.recId.setLayoutManager(new LinearLayoutManager(getActivity()));
        noteRecAdapter = new NoteAdapter();
        binding.recId.setAdapter(noteRecAdapter);
    }

    private void deleteAlertDialog(final Note note) {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Do you want to delete this note?")
                .setPositiveButton("delete", null)
                .setNegativeButton("Cancel", null)
                .show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            sharedViewModel.delete(note);
            dialog.dismiss();
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> dialog.dismiss());
    }

    private void attachAddNoteFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, new AddNoteFragment())
                .addToBackStack(null)
                .commit();
    }

    private void attachNoteUpdateFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, new NoteUpdateFragment())
                .addToBackStack(null)
                .commit();
    }
}
