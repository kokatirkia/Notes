package com.example.notes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.database.model.Note;
import com.example.notes.databinding.MainFragmentBinding;
import com.example.notes.ui.NoteAdapter;
import com.example.notes.ui.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {
    private MainFragmentBinding binding;
    private SharedViewModel sharedViewModel;
    private NoteAdapter noteRecAdapter;
    private List<Note> noteList;
    private FragmentActivity context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity() != null) context = getActivity();
        sharedViewModel = new ViewModelProvider(context).get(SharedViewModel.class);

        setUpRecyclerView();
        setUpObservers();
        setUpOnCLickListeners();
    }

    private void setUpRecyclerView() {
        binding.recId.setLayoutManager(new LinearLayoutManager(getActivity()));
        noteRecAdapter = new NoteAdapter();
        binding.recId.setAdapter(noteRecAdapter);
    }

    private void setUpObservers() {
        sharedViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
            noteRecAdapter.submitList(notes);
            noteList = new ArrayList<>(notes);
        });
    }

    private void setUpOnCLickListeners() {
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

        swipeToDeleteNote();

        binding.addNote.setOnClickListener(v -> attachAddNoteFragment());
    }

    private void swipeToDeleteNote() {
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
    }

    private void deleteAlertDialog(final Note note) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
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
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, new AddNoteFragment())
                .addToBackStack(null)
                .commit();
    }

    private void attachNoteUpdateFragment() {
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, new NoteUpdateFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_items, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                noteRecAdapter.submitList(sharedViewModel.filterNotes(query));
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            sharedViewModel.deleteAllNotes();
            return true;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
