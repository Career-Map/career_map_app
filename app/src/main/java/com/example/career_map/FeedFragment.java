package com.example.career_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.career_map.databinding.FragmentFeedBinding;

public class FeedFragment extends Fragment {

    private FragmentFeedBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFeedBinding.inflate(inflater, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) getParentFragment();
        if (navHostFragment != null) {
            StartFragment fragment = (StartFragment) navHostFragment.getParentFragment();
            if (fragment != null) {
                fragment.setTitleText(1);
            }
        }

        return binding.getRoot();
    }

    public void onClickViewAllEvents() {
        NavHostFragment navHostFragment = (NavHostFragment) getParentFragment();
        if (navHostFragment != null) {
            StartFragment startFragment = (StartFragment)navHostFragment.getParentFragment();
            if (startFragment != null) {
                startFragment.loadFragment(2);
            }
        }
    }


    public void onProfileImageClick() {
        NavHostFragment navHostFragment = (NavHostFragment) getParentFragment();
        if (navHostFragment != null) {
            StartFragment startFragment = (StartFragment)navHostFragment.getParentFragment();
            if (startFragment != null) {
                startFragment.loadFragment(5);
            }
        }
    }


}