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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notes.database.model.Note;
import com.example.notes.databinding.AddNoteFragmentBinding;
import com.example.notes.ui.SharedViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddNoteFragment extends Fragment {
    private AddNoteFragmentBinding binding;
    private SharedViewModel sharedViewModel;
    private FragmentActivity context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddNoteFragmentBinding.inflate(inflater, container, false);
        binding.title.requestFocus();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity() != null) context = getActivity();
        sharedViewModel = new ViewModelProvider(context).get(SharedViewModel.class);

        setUpOnClickListeners();
    }

    private void setUpOnClickListeners() {
        binding.saveButton.setOnClickListener(v -> {
            if (binding.title.getText().toString().isEmpty()
                    || binding.description.getText().toString().isEmpty()) {
                Toast.makeText(context, "Fill fields", Toast.LENGTH_SHORT).show();
            } else {
                sharedViewModel.insert(new Note(binding.title.getText().toString(),
                        binding.description.getText().toString()));
                popFromBackStack();
            }
        });
        binding.cancelButton.setOnClickListener(v -> popFromBackStack());
    }

    private void popFromBackStack() {
        context.getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
