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

import android.os.Handler;


public class ActivitiesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USR = "user";


    // TODO: Rename and change types of parameters




    private FragmentCompletedActivitiesBinding binding;

    private   RecyclerView.Adapter<ChoresViewHolder> adapter;

    Handler handler;



    public ActivitiesFragment() {
        // doesn't do anything special
    }

    public static ActivitiesFragment newInstance(String user) {
        ActivitiesFragment fragment = new ActivitiesFragment();
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

            ChildHomeActivity.choresCompletedDBReference.setChoresListener(ChildHomeActivity.listChoresCompleted,this.adapter);
            //ChildHome.choresCompletedDBReference.setChores(ChildHome.listChoresCompleted,this.adapter);
        }
        else if(getArguments().getString(USR).equals("childChoresToDo")){
            initializeRecyclerView2();

            ChildHomeActivity.toDoCompletedDBReference.setChoresListener(ChildHomeActivity.listChoresToDo,this.adapter);
            //ChildHome.toDoCompletedDBReference.setChores(ChildHome.listChoresToDo,this.adapter);
        }
        else if(getArguments().getString(USR).equals("parentChoresCompleted")){
            initializeRecyclerView3();

            ParentChildChoresActivity.choresCompletedDBReference.setChoresListener(ParentChildChoresActivity.listChoresCompleted,this.adapter);
            //ChildHome.choresCompletedDBReference.setChores(ChildHome.listChoresCompleted,this.adapter);
        }

        else if(getArguments().getString(USR).equals("parentChoresToDo")){
            initializeRecyclerView4();

            ParentChildChoresActivity.toDoCompletedDBReference.setChoresListener(ParentChildChoresActivity.listChoresToDo,this.adapter);
            //ChildHome.toDoCompletedDBReference.setChores(ChildHome.listChoresToDo,this.adapter);
        }





    }

    private void initializeRecyclerView1() {
        RecyclerView choresRecyclerView = binding.recyclerView;
        choresRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ChoresAdapter(ChildHomeActivity.listChoresCompleted, requireActivity(),"child", ChildHomeActivity.choresCompletedDBReference);
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

        adapter = new ChoresAdapter(ChildHomeActivity.listChoresToDo, requireActivity(),"child", ChildHomeActivity.toDoCompletedDBReference);
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

        adapter = new ChoresAdapter(ParentChildChoresActivity.listChoresCompleted, requireActivity(),"parent", ParentChildChoresActivity.choresCompletedDBReference);
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

        adapter = new ChoresAdapter(ParentChildChoresActivity.listChoresToDo, requireActivity(),"parent", ParentChildChoresActivity.toDoCompletedDBReference);
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