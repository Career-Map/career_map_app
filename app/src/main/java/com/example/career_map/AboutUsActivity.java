package com.example.career_map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.career_map.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends AppCompatActivity {

    private ActivityAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnCloseAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.tvMeetTheTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri teamLink = Uri.parse("https://thecareermap.in/");
                Intent intent = new Intent(Intent.ACTION_VIEW, teamLink);
                startActivity(intent);
            }
        });
    }
}