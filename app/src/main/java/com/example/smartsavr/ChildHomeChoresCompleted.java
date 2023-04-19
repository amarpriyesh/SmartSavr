package com.example.smartsavr;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;

import com.example.smartsavr.databinding.ActivityChildHomeChoresCompletedBinding;
import com.google.firebase.firestore.Query;

public class ChildHomeChoresCompleted extends AppCompatActivity {

    ActivityChildHomeChoresCompletedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChildHomeChoresCompletedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Query queryChoresCompleted = ChildHomeActivity.choresCompletedDBReference.getCollectionReference().whereEqualTo("childID", ChildHomeActivity.childId).whereEqualTo("complete", true).orderBy("completedTimestamp", Query.Direction.DESCENDING);
        ChildHomeActivity.choresCompletedDBReference.setQuery(queryChoresCompleted);
        setFragment(R.id.completedActivities,ChoresListFragment.newInstance("childChoresCompleted"));

    }

    private void setFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
}