package com.example.smartsavr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.graphics.drawable.Drawable;
import android.os.Bundle;


import com.example.smartsavr.databinding.ActivityChildHomeBinding;

import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;



import java.util.ArrayList;
import java.util.List;

public class ChildHome extends AppCompatActivity {


    static List<Chore> listChoresCompleted =new ArrayList<>();;
    static List<Chore> listChoresToDo = new ArrayList<>();

    static DBReference choresCompletedDBReference;
    static DBReference toDoCompletedDBReference;

    ChoresPoller poller;

    final String TAG = "FIREBASE QUERY";
    static ActivityChildHomeBinding binding;

    CalendarOperation cal;

    ActivitiesFragment completedActivityFragmnet;
    ActivitiesFragment toDoActivityFragmnet;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;

    String child;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        child="sam";
        listChoresCompleted.clear();
        listChoresToDo.clear();
        binding = ActivityChildHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("chores");



         choresCompletedDBReference = new DBReference(collectionReference,firebaseFirestore);
         toDoCompletedDBReference = new DBReference(collectionReference,firebaseFirestore);

         Query queryChoresCompleted = collectionReference.whereEqualTo("childID", child).whereEqualTo("complete",true).orderBy("completedTimestamp", Query.Direction.ASCENDING);
        Query queryChoresApproved = collectionReference.whereEqualTo("childID", child).whereEqualTo("complete",true).whereEqualTo("approved",true);

        choresCompletedDBReference.setQuery(queryChoresCompleted);
        choresCompletedDBReference.setQueryComplete(queryChoresApproved);
        choresCompletedDBReference.setApprovedListener();
        Query queryChoresToDo = collectionReference.whereEqualTo("childID", child).whereEqualTo("complete",false).orderBy("deadline", Query.Direction.ASCENDING);
        toDoCompletedDBReference.setQuery(queryChoresToDo);

        completedActivityFragmnet = ActivitiesFragment.newInstance("childChoresCompleted");
       toDoActivityFragmnet = ActivitiesFragment.newInstance("childChoresToDo");
        setFragment(R.id.fragmentCompletedActivities,(Fragment)completedActivityFragmnet);
        setFragment(R.id.fragmentUpcomingActivities,(Fragment)toDoActivityFragmnet);
        binding.imageView3.setImageResource(R.drawable.wallet1);


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



}