package com.example.smartsavr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.view.View;


import com.example.smartsavr.databinding.ActivityChildHomeBinding;

import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;



import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class ChildHome extends AppCompatActivity {


    static List<Chore> listChoresCompleted =new ArrayList<>();;
    static List<Chore> listChoresToDo = new ArrayList<>();

    static DBReference choresCompletedDBReference;
    static DBReference toDoCompletedDBReference;
    ChoresPoller poller;

    final String TAG = "FIREBASE QUERY";
    ActivityChildHomeBinding binding;

    CompletedActivitiesFragment completedActivityFragmnet;
    CompletedActivitiesFragment toDoActivityFragmnet;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listChoresCompleted.clear();
        listChoresToDo.clear();
        binding = ActivityChildHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("chores");
        setContentView(R.layout.activity_child_home);

         choresCompletedDBReference = new DBReference(collectionReference,firebaseFirestore);
         toDoCompletedDBReference = new DBReference(collectionReference,firebaseFirestore);

         Query queryChoresCompleted = collectionReference.whereEqualTo("childID", "12").whereEqualTo("complete",true).orderBy("deadline", Query.Direction.DESCENDING);
        choresCompletedDBReference.setQuery(queryChoresCompleted);
        Query queryChoresToDo = collectionReference.whereEqualTo("childID", "12").whereEqualTo("complete",false).orderBy("completedTimestamp", Query.Direction.ASCENDING);
        toDoCompletedDBReference.setQuery(queryChoresToDo);
        completedActivityFragmnet = CompletedActivitiesFragment.newInstance("childChoresCompleted");
       toDoActivityFragmnet = CompletedActivitiesFragment.newInstance("childChoresToDo");
        setFragment(R.id.fragmentCompletedActivities,(Fragment)completedActivityFragmnet);
        setFragment(R.id.fragmentUpcomingActivities,(Fragment)toDoActivityFragmnet);


    }

    private void setFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }








        void setCompleted(Chore chore) {
            collectionReference.document(chore.getId()).set(chore);

// ...
        }



}