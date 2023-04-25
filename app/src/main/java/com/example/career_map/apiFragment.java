package com.example.career_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.career_map.databinding.FragmentApiBinding;

public class apiFragment extends Fragment {
    private FragmentApiBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentApiBinding.inflate(inflater, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) getParentFragment();
        if (navHostFragment != null) {
            StartFragment fragment = (StartFragment) navHostFragment.getParentFragment();
            if (fragment != null) {
                fragment.setTitleText(3);
            }
        }

        return binding.getRoot();
    }

    public void onGoHomeOnBackPressed() {
        NavHostFragment navHostFragment = (NavHostFragment) getParentFragment();
        if (navHostFragment != null) {
            StartFragment fragment = (StartFragment) navHostFragment.getParentFragment();
            if (fragment != null) {
                fragment.loadFragment(1);
            }
        }
    }


}
