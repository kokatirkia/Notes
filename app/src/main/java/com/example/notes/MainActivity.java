package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notes.databinding.ActivityMainBinding;
import com.example.notes.databinding.AddNoteBinding;
import com.example.notes.database.model.Note;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        binding.recId.setLayoutManager(new LinearLayoutManager(this));
        final NoteAdapter noteRecAdapter = new NoteAdapter();
        binding.recId.setAdapter(noteRecAdapter);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                noteRecAdapter.submitList(notes);
            }
        });

        binding.addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteDialog();
            }
        });

        noteRecAdapter.itemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(final Note note) {
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Do you want to delete this note?")
                        .setPositiveButton("delete", null)
                        .setNegativeButton("Cancel", null)
                        .show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteViewModel.delete(note);
                        dialog.dismiss();
                    }
                });
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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
                noteViewModel.delete(noteRecAdapter.getNoteAtIndex(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(binding.recId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            noteViewModel.deleteAllNotes();
            return true;
        }
        return true;
    }

    private void addNoteDialog() {
        final AddNoteBinding bindingMeter = AddNoteBinding.inflate(getLayoutInflater());
        View view = bindingMeter.getRoot();

        final AlertDialog addMeterDialog = new AlertDialog.Builder(this)
                .setView(view)
                .show();
        bindingMeter.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindingMeter.title.getEditText().getText().toString().isEmpty()
                        || bindingMeter.description.getEditText().getText().toString().isEmpty()
                ) {
                    Toast.makeText(v.getContext(), "Fill Fields", Toast.LENGTH_SHORT).show();
                } else {
                    Note note = new Note(
                            bindingMeter.title.getEditText().getText().toString(),
                            bindingMeter.description.getEditText().getText().toString());
                    noteViewModel.insert(note);
                    addMeterDialog.dismiss();
                }
            }
        });
    }
}
