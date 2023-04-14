package com.example.smartsavr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.smartsavr.databinding.ActivityChildHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class ChildHome extends AppCompatActivity {

    ActivityChildHomeBinding binding;
    List<Chore> chores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildHomeBinding.inflate(getLayoutInflater());
        chores = new ArrayList<>();
        for(int i = 0; i<20; i++) {
            chores.add(new Chore(Integer.toString(i),Integer.toString(i),i,i,"Wash Dishes",i,true,i));
        }
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_child_home);
        setFragment(R.id.fragmentCompletedActivities,new CompletedActivitiesFragment(chores));
        setFragment(R.id.fragmentUpcomingActivities,new CompletedActivitiesFragment(chores));
    }

    private void setFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
}