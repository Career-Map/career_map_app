package com.example.career_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class courseFragment extends Fragment {
    public courseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        NavHostFragment navHostFragment = (NavHostFragment) getParentFragment();
        if (navHostFragment != null) {
            StartFragment fragment = (StartFragment) navHostFragment.getParentFragment();
            if (fragment != null) {
                fragment.setTitleText(2);
            }
        }

        return inflater.inflate(R.layout.fragment_course, container, false);
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
