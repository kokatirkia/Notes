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
import com.example.notes.databinding.AddNoteFragmentBinding;
import com.example.notes.ui.SharedViewModel;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Context.INPUT_METHOD_SERVICE;

@AndroidEntryPoint
public class AddNoteFragment extends Fragment {
    private AddNoteFragmentBinding binding;
    private SharedViewModel sharedViewModel;
    private FragmentActivity context;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddNoteFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        navController = Navigation.findNavController(view);
        if (getActivity() != null) context = getActivity();
        sharedViewModel = new ViewModelProvider(context).get(SharedViewModel.class);

        setUpOnClickListeners();
        setUpUi(savedInstanceState);
    }

    private void setUpUi(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            binding.title.setText(savedInstanceState.getString("title"));
            binding.title.setText(savedInstanceState.getString("description"));
        }
    }

    private void setUpOnClickListeners() {
        binding.saveButton.setOnClickListener(v -> {
            if (binding.title.getText().toString().isEmpty()
                    || binding.description.getText().toString().isEmpty()) {
                Toast.makeText(context, "Fill fields", Toast.LENGTH_SHORT).show();
            } else {
                sharedViewModel.insert(new Note(binding.title.getText().toString(),
                        binding.description.getText().toString()));
                hideKeyboard();
                navController.popBackStack();
            }
        });
        binding.cancelButton.setOnClickListener(v -> {
            hideKeyboard();
            navController.popBackStack();
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", binding.title.toString());
        outState.putString("description", binding.description.toString());
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
