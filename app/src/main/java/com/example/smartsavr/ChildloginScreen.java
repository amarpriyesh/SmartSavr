package com.example.smartsavr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smartsavr.databinding.ActivityChildloginScreenBinding;
import com.example.smartsavr.databinding.ActivityLoginBinding;

public class ChildloginScreen extends AppCompatActivity {

    ActivityChildloginScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildloginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}