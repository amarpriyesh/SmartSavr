package com.example.smartsavr;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartsavr.databinding.FragmentCompletedActivitiesBinding;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;


public class CompletedActivitiesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USR = "user";


    // TODO: Rename and change types of parameters




    private FragmentCompletedActivitiesBinding binding;

    private   RecyclerView.Adapter<ChoresViewHolder> adapter;

    Handler handler;



    public CompletedActivitiesFragment() {
        // doesn't do anything special
    }

    public static CompletedActivitiesFragment newInstance(String user) {
        CompletedActivitiesFragment fragment = new CompletedActivitiesFragment();
        Bundle args = new Bundle();
        args.putString(USR, user);
        fragment.setArguments(args);
        return fragment;
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* binding = FragmentCompletedActivitiesBinding.inflate(getLayoutInflater());
        initializeRecyclerView();*/


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
        Log.d("GET STRING",getArguments().getString(USR));
        if(getArguments().getString(USR).equals("childChoresCompleted")){
            initializeRecyclerView1();

            ChildHome.choresCompletedDBReference.setChoresListener(ChildHome.listChoresCompleted,this.adapter);
            //ChildHome.choresCompletedDBReference.setChores(ChildHome.listChoresCompleted,this.adapter);
        }
        else if(getArguments().getString(USR).equals("childChoresToDo")){
            initializeRecyclerView2();

            ChildHome.toDoCompletedDBReference.setChoresListener(ChildHome.listChoresToDo,this.adapter);
            //ChildHome.toDoCompletedDBReference.setChores(ChildHome.listChoresToDo,this.adapter);
        }
        else if(getArguments().getString(USR).equals("parentChoresCompleted")){
            initializeRecyclerView3();

            ParentTaskView.choresCompletedDBReference.setChoresListener(ParentTaskView.listChoresCompleted,this.adapter);
            //ChildHome.choresCompletedDBReference.setChores(ChildHome.listChoresCompleted,this.adapter);
        }

        else if(getArguments().getString(USR).equals("parentChoresToDo")){
            initializeRecyclerView4();

            ParentTaskView.toDoCompletedDBReference.setChoresListener(ParentTaskView.listChoresToDo,this.adapter);
            //ChildHome.toDoCompletedDBReference.setChores(ChildHome.listChoresToDo,this.adapter);
        }





    }

    private void initializeRecyclerView1() {
        RecyclerView choresRecyclerView = binding.recyclerView;
        choresRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ChoresAdapter(ChildHome.listChoresCompleted, requireActivity(),"child",ChildHome.choresCompletedDBReference);
        // Associates the adapter with the RecyclerView
        choresRecyclerView.setAdapter(adapter);
        handler = new Handler();
        Runnable newRUnnable = new ChoresPoller(adapter,false,handler);
        new Thread(newRUnnable).start();
        /*Runnable runnable = new Runnable() {
            @Override
            public void run() {



                handler.postDelayed(this, 1000);
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(runnable);*/
    }

    private void initializeRecyclerView2() {
        RecyclerView choresRecyclerView = binding.recyclerView;
        choresRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ChoresAdapter(ChildHome.listChoresToDo, requireActivity(),"child",ChildHome.toDoCompletedDBReference);
        // Associates the adapter with the RecyclerView
        choresRecyclerView.setAdapter(adapter);


        handler = new Handler();
        Runnable newRUnnable = new ChoresPoller(adapter,false,handler);
        new Thread(newRUnnable).start();


       /* Runnable runnable = new Runnable() {
            @Override
            public void run() {



                handler.postDelayed(this, 1000);
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(runnable);*/

    }
    private void initializeRecyclerView3() {
        RecyclerView choresRecyclerView = binding.recyclerView;
        choresRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ChoresAdapter(ParentTaskView.listChoresCompleted, requireActivity(),"parent",ParentTaskView.choresCompletedDBReference);
        // Associates the adapter with the RecyclerView
        choresRecyclerView.setAdapter(adapter);
        handler = new Handler();
        Runnable newRUnnable = new ChoresPoller(adapter,false,handler);
        new Thread(newRUnnable).start();
        /*Runnable runnable = new Runnable() {
            @Override
            public void run() {



                handler.postDelayed(this, 1000);
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(runnable);*/
    }

    private void initializeRecyclerView4() {
        RecyclerView choresRecyclerView = binding.recyclerView;
        choresRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ChoresAdapter(ParentTaskView.listChoresToDo, requireActivity(),"parent",ParentTaskView.toDoCompletedDBReference);
        // Associates the adapter with the RecyclerView
        choresRecyclerView.setAdapter(adapter);


        handler = new Handler();
        Runnable newRUnnable = new ChoresPoller(adapter,false,handler);
        new Thread(newRUnnable).start();


       /* Runnable runnable = new Runnable() {
            @Override
            public void run() {



                handler.postDelayed(this, 1000);
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(runnable);*/

    }
    public RecyclerView.Adapter<ChoresViewHolder> getAdapter(){
        return this.adapter;
    }
}