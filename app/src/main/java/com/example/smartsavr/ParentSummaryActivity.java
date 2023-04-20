package com.example.smartsavr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;

import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ParentSummaryActivity extends AppCompatActivity {

    static final long DAY_INCREMENT = 1000 * 60 * 60 * 24;

    LineChart lineChart;
    FirebaseFirestore db;
    CollectionReference chores;
    CollectionReference children;

    ChildChartData childChartData;
    ParentChartData parentChartData;

    ArrayList<String> children_ids = new ArrayList<String>();
    ArrayList<String> children_names = new ArrayList<String>();
    List<Chore> listApprovedChores = new ArrayList<>();

    ArrayList<Switch> switches = new ArrayList<Switch>();
    ArrayList<Integer> colors = new ArrayList<Integer>();
    List<String> xAxisBottomLabels = new ArrayList<String>();
    List<ILineDataSet> childrenData = new ArrayList<>();

    static ArrayList<Entry> values = new ArrayList<>();

    private final String TAG = "DB ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_summary);

        parentChartData = new ParentChartData("12a.pradhan@gmail.com");
        parentChartData.setChildrenList(children_ids -> {
            children_ids = parentChartData.getChildren_ids();
            children_names = parentChartData.getChildren_names();
        });

    interface DBCallback {
        void onCallback(List<String> childIds);
    }



}