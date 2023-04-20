package com.example.smartsavr;

import android.util.Log;
import android.widget.Switch;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class ParentChartData {
    FirebaseFirestore db;
    CollectionReference chores;
    CollectionReference children;
    DBCallback dbCallback;
    ChildChartData childChartData;
    String parentId;
    ArrayList<String> children_ids = new ArrayList<String>();
    ArrayList<String> children_names = new ArrayList<String>();
    List<Chore> listApprovedChores = new ArrayList<>();
    ArrayList<Entry> values = new ArrayList<Entry>();

    private final String TAG = "DB ERROR";

    public ParentChartData(String parentId) {
        this.parentId = parentId;
    }

    public ArrayList<String> getChildren_ids() {
        return this.children_ids;
    }

    public ArrayList<String> getChildren_names() {
        return this.children_names;
    }

    public void setChoresList() {
        listApprovedChores.clear();

        db = FirebaseFirestore.getInstance();
        chores = db.collection("chores");

        Query query = chores.whereEqualTo("complete",true).orderBy("completedTimestamp", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (QueryDocumentSnapshot document : snapshot) {
                    Chore chore = document.toObject(Chore.class);
                    listApprovedChores.add(chore);
                }
            } else {
                Log.e(TAG, "Database error when loading documents");
            }
        });

    }

    public void setChildrenList(DBCallback dbCallback) {
        db = FirebaseFirestore.getInstance();
        children = db.collection("children");

        //TODO change query back to correct conditions
        Query query = children.whereEqualTo("parentId", "12a.pradhan@gmail.com").orderBy("name", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (QueryDocumentSnapshot document : snapshot) {
                    children_ids.add(document.getId());
                    children_names.add(document.getString("name"));
                }
                dbCallback.onCallback(children_ids);
            } else {
                Log.e(TAG, "Database error when loading documents");
            }
        });
    }

    interface DBCallback {
        void onCallback(ArrayList<String> children_ids);
    }

}
