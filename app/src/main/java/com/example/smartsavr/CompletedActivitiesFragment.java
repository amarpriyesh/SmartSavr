package com.example.smartsavr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartsavr.databinding.FragmentCompletedActivitiesBinding;

import java.util.ArrayList;
import java.util.List;


public class CompletedActivitiesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Chore choreModel;

    private FragmentCompletedActivitiesBinding binding;

    private RecyclerView.Adapter<ChoresViewHolder> adapter;

    List<Chore> chores;



    public CompletedActivitiesFragment(List<Chore> chores) {
        this.chores = chores;
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
        binding = FragmentCompletedActivitiesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        RecyclerView choresRecyclerView = binding.recyclerView;
        choresRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ChoresAdapter(chores, requireActivity(),"child");
        // Associates the adapter with the RecyclerView
        choresRecyclerView.setAdapter(adapter);
    }
}