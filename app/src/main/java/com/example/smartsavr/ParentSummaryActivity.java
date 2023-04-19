package com.example.smartsavr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ParentSummaryActivity extends AppCompatActivity {

    static final long DAY_INCREMENT = 1000 * 60 * 60 * 24;

    Switch switch1;
    Switch switch2;
    Switch switch3;
    Switch switch4;
    LineChart lineChart;
    FirebaseFirestore db;

    ArrayList<String> children_ids = new ArrayList<String>();
    ArrayList<String> children_names = new ArrayList<String>();
    ArrayList<Switch> switches = new ArrayList<Switch>();
    ArrayList<Integer> colors = new ArrayList<Integer>();
    List<ILineDataSet> children = new ArrayList<>();
    ArrayList<Entry> values = new ArrayList<>();
    ArrayList<Entry> values1 = new ArrayList<>();
    ArrayList<Entry> values2 = new ArrayList<>();
    ArrayList<Entry> values3 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_summary);

        lineChart = findViewById(R.id.lineChartParent);

        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(0f, 3.5f));
        values.add(new Entry(1f, 1f));
        values.add(new Entry(2f, 5f));
        values.add(new Entry(3f, 2.5f));
        values.add(new Entry(4f, 3f));
        values.add(new Entry(5f, Float.NaN));
        values.add(new Entry(6f, Float.NaN));
        LineDataSet child1 = new LineDataSet(values,"Jim");
        children.add(child1);


        ArrayList<Entry> values2 = new ArrayList<>();
        values2.add(new Entry(0f, 1.5f));
        values2.add(new Entry(1f, 0f));
        values2.add(new Entry(2f, 3f));
        values2.add(new Entry(3f, 1f));
        values2.add(new Entry(4f, 2f));
        values2.add(new Entry(5f, Float.NaN));
        values2.add(new Entry(6f, Float.NaN));
        LineDataSet child2 = new LineDataSet(values2,"John");
        children.add(child2);


        ArrayList<Entry> values3 = new ArrayList<>();
        values3.add(new Entry(0f, 1f));
        values3.add(new Entry(1f, 2f));
        values3.add(new Entry(2f, 5f));
        values3.add(new Entry(3f, 4f));
        values3.add(new Entry(4f, 5f));
        values3.add(new Entry(5f, 4f));


        ArrayList<Entry> values4 = new ArrayList<>();
        values4.add(new Entry(0f, 5f));
        values4.add(new Entry(1f, 4f));
        values4.add(new Entry(2f, 3f));
        values4.add(new Entry(3f, 2f));
        values4.add(new Entry(4f, 2f));
        values4.add(new Entry(5f, Float.NaN));
        values4.add(new Entry(6f, Float.NaN));

        colors.add(Color.BLUE);
        colors.add(Color.MAGENTA);
        colors.add(Color.GREEN);
        colors.add(Color.RED);

        switch1 = findViewById(R.id.switch1);
        switch1.setText(child1.getLabel());
        switch2 = findViewById(R.id.switch2);
        switch2.setText(child2.getLabel());
        switch3 = findViewById(R.id.switch3);
        switch4 = findViewById(R.id.switch4);

        switches.add(switch1);
        switches.add(switch2);
        switches.add(switch3);
        switches.add(switch4);

        for (int i = 0; i < 2; i++) {
            switches.get(i).setVisibility(View.VISIBLE);
            switches.get(i).setChecked(true);
        }



        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    child1.setVisible(true);
                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                } else {
                    child1.setVisible(false);
                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    child2.setVisible(true);
                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                } else {
                    child2.setVisible(false);
                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                }
            }
        });


        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("04-16");
        xAxisLabel.add("04-17");
        xAxisLabel.add("04-18");
        xAxisLabel.add("04-19");
        xAxisLabel.add("04-20");
        xAxisLabel.add("04-21");
        xAxisLabel.add("04-22");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(xAxisLabel.size(), true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
//        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        YAxis yAxis = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxis.setAxisMinimum(0);
        yAxisRight.setAxisMinimum(0);
        LineData data = new LineData(children);
        lineChart.setData(data);
        lineChart.invalidate();
        lineChart.setHorizontalScrollBarEnabled(true);
        lineChart.setVisibleXRange(7,7);
        lineChart.moveViewToX(values.size()-7);
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.invalidate();
    }

    public ArrayList<Entry> showChildDataSet(int index) {
        if (index == 0) {
            return values;
        } else if (index == 1) {
            return values1;
        } else if (index == 2) {
            return values2;
        } else if (index == 3) {
            return values3;
        }
        return null;
    }


}