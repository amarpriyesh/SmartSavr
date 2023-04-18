package com.example.smartsavr;

import static com.example.smartsavr.Utils.centsToDollarString;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.smartsavr.databinding.ActivityChildHomeBinding;

import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;



import java.util.ArrayList;
import java.util.List;

public class ChildHomeActivity extends AppCompatActivity {


    static List<Chore> listChoresCompleted =new ArrayList<>();
    static List<Chore> listChoresToDo = new ArrayList<>();

    static DBReference choresCompletedDBReference;
    static DBReference toDoCompletedDBReference;

    ChoresPoller poller;

    final String TAG = "FIREBASE QUERY";
    ActivityChildHomeBinding binding;

    CalendarOperation cal;

    ChoresListFragment completedActivityFragmnet;
    ChoresListFragment toDoActivityFragmnet;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;

    String child;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_home);
        }

        child = "sam";
        listChoresCompleted.clear();
        listChoresToDo.clear();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("chores");


        choresCompletedDBReference = new DBReference(collectionReference, firebaseFirestore);
        toDoCompletedDBReference = new DBReference(collectionReference, firebaseFirestore);

        Query queryChoresCompleted = collectionReference.whereEqualTo("childID", child).whereEqualTo("complete", true).orderBy("completedTimestamp", Query.Direction.ASCENDING);
        Query queryChoresApproved = collectionReference.whereEqualTo("childID", child).whereEqualTo("complete", true).whereEqualTo("approved", true);

        choresCompletedDBReference.setQuery(queryChoresCompleted);
        choresCompletedDBReference.setQueryComplete(queryChoresApproved);
        choresCompletedDBReference.setApprovedListener((total, sumWeekly, sumMonthly) -> {
            binding.textTotal.setText(centsToDollarString(total));
            binding.textWeekly.setText(getResources().getString(R.string.weekly_earnings, centsToDollarString(sumWeekly)));
            binding.textMonthly.setText(getResources().getString(R.string.monthly_earnings, centsToDollarString(sumMonthly)));
        });
        Query queryChoresToDo = collectionReference.whereEqualTo("childID", child).whereEqualTo("complete", false).orderBy("deadline", Query.Direction.ASCENDING);
        toDoCompletedDBReference.setQuery(queryChoresToDo);

        completedActivityFragmnet = ChoresListFragment.newInstance("childChoresCompleted");
        toDoActivityFragmnet = ChoresListFragment.newInstance("childChoresToDo");
        setFragment(R.id.fragmentCompletedActivities, completedActivityFragmnet);
        setFragment(R.id.fragmentUpcomingActivities, toDoActivityFragmnet);
    }

    private void setFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

        void setTable() {

// ...
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
