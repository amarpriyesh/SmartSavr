package com.example.smartsavr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.smartsavr.databinding.ActivityParentTaskViewBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ParentTaskView extends AppCompatActivity {

    List<Chore> chores;

    ActivityParentTaskViewBinding binding;

    FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_task_view);
        binding = ActivityParentTaskViewBinding.inflate(getLayoutInflater());
        firebaseFirestore = FirebaseFirestore.getInstance();
        setFragment(R.id.fragmentNeedApproval,new CompletedActivitiesFragment(chores));
        setFragment(R.id.fragmentCompletedActivities,new CompletedActivitiesFragment(chores));

    }

    private void setFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }


}