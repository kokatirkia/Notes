package com.example.notes.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.R;
import com.example.notes.databinding.ActivityMainBinding;
import com.example.notes.ui.fragments.MainFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        attachMainFragment(savedInstanceState);
    }

    private void attachMainFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_placeholder, new MainFragment()).commit();
        }
    }
}
