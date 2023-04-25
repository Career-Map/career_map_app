package com.example.career_map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.career_map.Utils.Constant;
import com.example.career_map.databinding.FragmentFaqBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FAQFragment extends Fragment {

    private FragmentFaqBinding binding;
    private NavController navController;
    private FirebaseFirestore firestoreDB;

    private FAQAdapter adapter;
    private Constant constant;
    private CollectionReference collectionReference;
    private FirestoreRecyclerOptions<FAQModel> options;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFaqBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.feedbackFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_aboutUsFragment_to_feedbackFragment);
            }
        });

        binding.btnCloseFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        constant = new Constant();
        firestoreDB = FirebaseFirestore.getInstance();
        collectionReference = firestoreDB.collection(constant.getFAQ());

        initData();
        initRecycler();

        return view;
    }

    private void initData() {
        Query query = collectionReference.orderBy(constant.getFaqId());

        options = new FirestoreRecyclerOptions.Builder<FAQModel>()
                .setQuery(query, FAQModel.class)
                .build();
    }

    private void initRecycler() {
        binding.FAQRecyclerview.setHasFixedSize(true);
        adapter = new FAQAdapter(options, getContext());
        binding.FAQRecyclerview.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        binding.FAQRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && binding.feedbackFab.getVisibility() == View.VISIBLE) {
                    binding.feedbackFab.hide();
                } else if (dy < 0 && binding.feedbackFab.getVisibility() != View.VISIBLE) {
                    binding.feedbackFab.show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}