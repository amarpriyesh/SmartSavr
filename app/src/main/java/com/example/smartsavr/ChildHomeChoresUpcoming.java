package com.example.smartsavr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.smartsavr.databinding.ActivityChildHomeChoresUpcomingBinding;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChildHomeChoresUpcoming extends AppCompatActivity {

    ActivityChildHomeChoresUpcomingBinding binding;

    static List<Chore> listChoresUpcomingAll =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildHomeChoresUpcomingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Query queryChoresUpcoming = ChildHomeActivity.toDoCompletedDBReference.getCollectionReference().whereEqualTo("childID", ChildHomeActivity.childId).whereEqualTo("complete", false).orderBy("deadline", Query.Direction.ASCENDING).orderBy("assignedTimestamp",Query.Direction.DESCENDING);
        ChildHomeActivity.toDoCompletedDBReference.setQuery(queryChoresUpcoming);
        setFragment(R.id.upcomingActivities,ChoresListFragment.newInstance("childChoresToDoAll"));
    }

    private void setFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
}