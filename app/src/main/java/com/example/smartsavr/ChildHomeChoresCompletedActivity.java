package com.example.smartsavr;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smartsavr.databinding.ActivityChildHomeChoresCompletedBinding;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChildHomeChoresCompletedActivity extends AppCompatActivity {

    ActivityChildHomeChoresCompletedBinding binding;
    static List<Chore> listChoresCompletedAll = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChildHomeChoresCompletedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.completed_chores);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Query queryChoresCompleted = ChildHomeActivity.choresCompletedDBReference.getCollectionReference().whereEqualTo("childID", ChildHomeActivity.childId).whereEqualTo("complete", true).orderBy("completedTimestamp", Query.Direction.DESCENDING);
        ChildHomeActivity.choresCompletedDBReference.setQuery(queryChoresCompleted);
        setFragment(R.id.completedActivities,ChoresListFragment.newInstance("childChoresCompletedAll"));

    }

    private void setFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
}