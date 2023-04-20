package com.example.smartsavr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.smartsavr.databinding.ActivityChildHomeChoresUpcomingBinding;
import com.google.firebase.firestore.Query;

public class ChildHomeChoresUpcomingActivity extends AppCompatActivity {

    ActivityChildHomeChoresUpcomingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildHomeChoresUpcomingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.upcoming_chores);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Query queryChoresUpcoming = ChildHomeActivity.toDoCompletedDBReference.getCollectionReference().whereEqualTo("childID", ChildHomeActivity.childId).whereEqualTo("complete", false).orderBy("deadline", Query.Direction.ASCENDING);
        ChildHomeActivity.toDoCompletedDBReference.setQuery(queryChoresUpcoming);
        setFragment(R.id.upcomingActivities,ChoresListFragment.newInstance("childChoresToDo"));
    }

    private void setFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
}