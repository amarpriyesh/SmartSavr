package com.example.smartsavr;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DBReference {

    FirebaseFirestore firebaseFirestore;

    CollectionReference collectionReference;

    Query query;

    final String TAG = "DB ERROR";

    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }

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

    }


    public void setChores(List<Chore> chores, RecyclerView.Adapter<ChoresViewHolder> adapter){
        chores.clear();
        List<Chore> tmpList = new ArrayList<>();
        Task<QuerySnapshot> t=  this.query.get();
        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Task Was  successful: "+ Integer.toString(task.getResult().size()));
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Chore obj = document.toObject(Chore.class);
                        chores.add(obj);


                    }
                    adapter.notifyDataSetChanged();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    public void setChoresListener(List<Chore> chores, RecyclerView.Adapter<ChoresViewHolder> adapter) {
        this.query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.err.println("Listen failed: " + error);
                    return;
                }
                chores.clear();

                for (DocumentSnapshot ds : value.getDocuments()) {
                    //TODO handle exception
                    Chore obj = ds.toObject(Chore.class);
                    Log.d(TAG,ds.getData().toString());
                    chores.add(obj);
                }
               adapter.notifyDataSetChanged();
            }
        });


    }
    void setCompleted(Chore chore) {
        collectionReference.document(chore.getId()).set(chore);

// ...
    }

    void setQuery(Query query){
        this.query = query;
    }



}


