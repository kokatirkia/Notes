package com.example.notes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notes.database.model.Note;
import com.example.notes.databinding.NoteUpdateFragmentBinding;
import com.example.notes.ui.SharedViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NoteUpdateFragment extends Fragment {
    private NoteUpdateFragmentBinding binding;
    private SharedViewModel sharedViewModel;
    private Note updateNote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NoteUpdateFragmentBinding.inflate(inflater, container, false);
        binding.updateDescription.requestFocus();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        sharedViewModel.getNote().observe(getViewLifecycleOwner(), note -> {
            updateNote = note;
            binding.updateTitle.setText(note.getTitle());
            binding.updateDescription.setText(note.getDescription());
        });

        binding.updateSaveButton.setOnClickListener(v -> {
            if (binding.updateTitle.getText().toString().isEmpty()
                    || binding.updateDescription.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Fill fields", Toast.LENGTH_SHORT).show();
            } else {
                updateNote.setTitle(binding.updateTitle.getText().toString());
                updateNote.setDescription(binding.updateDescription.getText().toString());
                sharedViewModel.update(updateNote);
                popFromBackStack();
            }
        });
        binding.updateCancelButton.setOnClickListener(v -> popFromBackStack());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void popFromBackStack() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
