package com.example.smartsavr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.smartsavr.databinding.ActivityChildHomeBinding;
import com.example.smartsavr.databinding.ActivityParentTaskViewBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ParentTaskView extends AppCompatActivity {

    static List<Chore> listChoresCompleted =new ArrayList<>();;
    static List<Chore> listChoresToDo = new ArrayList<>();

    static DBReference choresCompletedDBReference;
    static DBReference toDoCompletedDBReference;

    static DBReference choresAddDBReference;

    static FragmentManager fragmentManager;

    static String childID;
    ChoresPoller poller;

    final String TAG = "FIREBASE QUERY";
    ActivityParentTaskViewBinding binding;

    CompletedActivitiesFragment completedActivityFragmnet;
    CompletedActivitiesFragment toDoActivityFragmnet;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        fragmentManager = getSupportFragmentManager();
        childID = intent.getExtras().getString("child");
        listChoresCompleted.clear();
        listChoresToDo.clear();
        binding = ActivityParentTaskViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("chores");
        //setContentView(R.layout.activity_parent_task_view);

        choresCompletedDBReference = new DBReference(collectionReference,firebaseFirestore);
        toDoCompletedDBReference = new DBReference(collectionReference,firebaseFirestore);

        Query queryChoresCompleted = collectionReference.whereEqualTo("childID", childID).whereEqualTo("complete",true).orderBy("completedTimestamp", Query.Direction.ASCENDING);
        choresCompletedDBReference.setQuery(queryChoresCompleted);
        Query queryChoresToDo = collectionReference.whereEqualTo("childID", childID).whereEqualTo("complete",false).orderBy("deadline", Query.Direction.ASCENDING);
        toDoCompletedDBReference.setQuery(queryChoresToDo);
        completedActivityFragmnet = CompletedActivitiesFragment.newInstance("parentChoresCompleted");
        toDoActivityFragmnet = CompletedActivitiesFragment.newInstance("parentChoresToDo");
        setFragment(R.id.fragmentNeedApproval,(Fragment)completedActivityFragmnet);
        setFragment(R.id.fragmentCompletedActivities,(Fragment)toDoActivityFragmnet);
        setClickListeners();


    }

    private void setFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    private void setClickListeners() {
        // todo: move this functionality to the child chore management screen when it's implemented
        binding.addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    ChoreBottomSheetDialog bottomSheet = new ChoreBottomSheetDialog();
                    bottomSheet.show(fragmentManager, ChoreBottomSheetDialog.TAG);



            }
        });
    }

    public static FragmentManager getSupportFragmentManagerParent(){
        return fragmentManager;
    }


}