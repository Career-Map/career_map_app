package com.example.career_map;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.career_map.databinding.ActivityFaqBinding;

public class FAQActivity extends AppCompatActivity {

    private ActivityFaqBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFaqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}