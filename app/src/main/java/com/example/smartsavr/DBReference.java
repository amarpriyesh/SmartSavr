package com.example.smartsavr;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartsavr.databinding.ActivityChildHomeBinding;
import com.example.smartsavr.databinding.FragmentChoresListBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DBReference {

    FirebaseFirestore firebaseFirestore;

    public CollectionReference collectionReference;

    Query query;

    Query queryComplete;

    final String TAG = "DB ERROR";

    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }

    private final CalendarOperation cal;



    public void setFirebaseFirestore(FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    public CollectionReference getCollectionReference() {
        return collectionReference;
    }

    public void setCollectionReference(CollectionReference collectionReference) {
        this.collectionReference = collectionReference;
    }

    public DBReference(CollectionReference collectionReference, FirebaseFirestore firebaseFirestore ){
        this.firebaseFirestore = firebaseFirestore;
        this.collectionReference  =  collectionReference;
        cal = new CalendarOperation();

    }


    public void setChores(List<Chore> chores, RecyclerView.Adapter<ChoresViewHolder> adapter){
        chores.clear();
        List<Chore> tmpList = new ArrayList<>();
        Task<QuerySnapshot> t=  this.query.get();
        t.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Task Was  successful: "+ task.getResult().size());
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Chore obj = document.toObject(Chore.class);
                    chores.add(obj);


                }
                adapter.notifyDataSetChanged();

            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

    }


    public void setChoresListener(List<Chore> chores, RecyclerView.Adapter<ChoresViewHolder> adapter, FragmentChoresListBinding binding) {
        this.query.addSnapshotListener((value, error) -> {
            if (error != null) {

                System.err.println("Listen failed: " + error);
                return;
            }
            chores.clear();



            for (DocumentSnapshot ds : value.getDocuments()) {
                //TODO handle exception
                Chore obj = ds.toObject(Chore.class);
                chores.add(obj);
            }
            adapter.notifyDataSetChanged();


            if (chores.isEmpty()) {
                binding.noChoresTextView.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.noChoresTextView.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }

        });



    }

    public void setChoresListenerSeeTextUpcoming(Query query, ActivityChildHomeBinding binding) {
        query.addSnapshotListener((value, error) -> {
            if (error != null) {

                System.err.println("Listen failed: " + error);
                return;
            }

            if  (value.size()>3) {
                binding.linkUpcomingActivities.setVisibility(View.VISIBLE);
            }
            else {
                binding.linkUpcomingActivities.setVisibility(View.GONE);
            }

        });



    }

    public void setChoresListenerSeeTextCompleted(Query query, ActivityChildHomeBinding binding) {
        query.addSnapshotListener((value, error) -> {
            if (error != null) {

                System.err.println("Listen failed: " + error);
                return;
            }

            if  (value.size()>3) {
                binding.linkCompletedActivities.setVisibility(View.VISIBLE);
            }
            else {
                binding.linkCompletedActivities.setVisibility(View.GONE);
            }

        });



    }

    public void setApprovedListener(EarningsBalanceConsumer earningsBalanceConsumer) {
        this.queryComplete.addSnapshotListener((value, error) -> {
            if (error != null) {
                System.err.println("Listen failed: " + error);
                return;
            }

            int sumMonthly = 0;
            int sumWeekly = 0;
          AtomicInteger totalBalance = new AtomicInteger();



            for (DocumentSnapshot ds : value.getDocuments()) {
                //TODO handle exception
                Chore obj = ds.toObject(Chore.class);
                Log.d(TAG,ds.getData().toString());
                if(obj.isComplete()) {

                    if (obj.isApproved() && obj.getApprovedTimestamp() > cal.calMillisWeek()) {
                        sumWeekly += obj.getRewardCents();
                    }
                    if (obj.isApproved() && obj.getApprovedTimestamp() > cal.calMillisMonth()) {
                        sumMonthly += obj.getRewardCents();
                    }

                }
            }






            earningsBalanceConsumer.accept(totalBalance.get(),sumWeekly, sumMonthly);



        });


    }
    void setCompleted(Chore chore) {
        collectionReference.document(chore.getId()).set(chore);

// ...
    }

    void setQuery(Query query){
        this.query = query;
    }

    void setQueryComplete(Query query){
        this.queryComplete = query;
    }



}


