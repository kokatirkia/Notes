package com.example.notes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.notes.database.model.Note;
import com.example.notes.databinding.NoteUpdateFragmentBinding;
import com.example.notes.ui.SharedViewModel;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Context.INPUT_METHOD_SERVICE;

@AndroidEntryPoint
public class NoteUpdateFragment extends Fragment {
    private NoteUpdateFragmentBinding binding;
    private SharedViewModel sharedViewModel;
    private Note updateNote;
    private FragmentActivity context;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NoteUpdateFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        navController = Navigation.findNavController(view);
        if (getActivity() != null) context = getActivity();
        sharedViewModel = new ViewModelProvider(context).get(SharedViewModel.class);
        updateNote = sharedViewModel.getNote();

        setUpOnClickListener();
        setUpUi(savedInstanceState);
    }

    private void setUpUi(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            binding.updateTitle.setText(savedInstanceState.getString("title"));
            binding.updateDescription.setText(savedInstanceState.getString("description"));
        } else {
            binding.updateTitle.setText(updateNote.getTitle());
            binding.updateDescription.setText(updateNote.getDescription());
        }
    }

    private void setUpOnClickListener() {
        binding.updateSaveButton.setOnClickListener(v -> {
            if (binding.updateTitle.getText().toString().isEmpty()
                    || binding.updateDescription.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Fill fields", Toast.LENGTH_SHORT).show();
            } else {
                updateNote.setTitle(binding.updateTitle.getText().toString());
                updateNote.setDescription(binding.updateDescription.getText().toString());
                sharedViewModel.update(updateNote);
                hideKeyboard();
                navController.popBackStack();
            }
        });
        binding.updateCancelButton.setOnClickListener(v -> {
            hideKeyboard();
            navController.popBackStack();
        });
    }

    private void hideKeyboard() {
        if (context.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", binding.updateTitle.toString());
        outState.putString("description", binding.updateDescription.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
