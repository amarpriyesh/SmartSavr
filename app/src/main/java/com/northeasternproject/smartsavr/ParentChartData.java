package com.northeasternproject.smartsavr;

import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ParentChartData {
    static final long CENTS = 100;
    FirebaseFirestore db;
    CollectionReference children;
    String parentId;

    private final String TAG = "DB ERROR";

    public ParentChartData(String parentId) {
        this.parentId = parentId;
    }

    static ArrayList<String> children_names = new ArrayList<>();
    static ArrayList<Entry> allowance = new ArrayList<>();
    static ArrayList<BarEntry> balanceBar = new ArrayList<>();
    static ArrayList<PieEntry> balancePie = new ArrayList<>();

    public void setChartData(DBCallback dbCallback) {
        db = FirebaseFirestore.getInstance();
        children = db.collection("children");

        //TODO change query back to correct conditions
        allowance.clear();
        balanceBar.clear();
        balancePie.clear();
        children_names.clear();
        Query query = children.whereEqualTo("parentId", parentId).orderBy("name", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                int i = 0;
                for (QueryDocumentSnapshot document : snapshot) {
                    Child child = document.toObject(Child.class);
                    allowance.add(new Entry(i, (float) child.getWeeklyAllowanceCents() / CENTS));
                    balanceBar.add(new BarEntry(i, (float) child.getAccountBalanceCents() / CENTS));
                    if (child.getAccountBalanceCents() > 0) {
                        balancePie.add(new PieEntry((float) child.getAccountBalanceCents() / CENTS, child.getName()));
                    }
                    children_names.add(child.getName());
                    i++;
                }
                dbCallback.onCallback(children_names);
            } else {
                Log.e(TAG, "Database error when loading documents");
            }
        });
    }

    public ArrayList<Entry> getAllowance() {
        return allowance;
    }

    public ArrayList<BarEntry> getBalanceBar() {
        return balanceBar;
    }

    public ArrayList<PieEntry> getBalancePie() {
        return balancePie;
    }

    public ArrayList<String> getChildrenNames() {
        return children_names;
    }

    interface DBCallback {
        void onCallback(ArrayList<String> children_names);
    }
}
