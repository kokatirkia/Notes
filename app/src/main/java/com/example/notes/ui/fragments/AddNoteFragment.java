package com.example.notes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notes.database.model.Note;
import com.example.notes.databinding.AddNoteFragmentBinding;
import com.example.notes.ui.SharedViewModel;

public class AddNoteFragment extends Fragment {
    private AddNoteFragmentBinding binding;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddNoteFragmentBinding.inflate(inflater, container, false);
        binding.title.requestFocus();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.title.getText().toString().isEmpty()
                        || binding.description.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Fill fields", Toast.LENGTH_SHORT).show();
                } else {
                    sharedViewModel.insert(new Note(binding.title.getText().toString(),
                            binding.description.getText().toString()));
                    popFromBackStack();
                }
            }
        });
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFromBackStack();
            }
        });
    }

    private void popFromBackStack() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}