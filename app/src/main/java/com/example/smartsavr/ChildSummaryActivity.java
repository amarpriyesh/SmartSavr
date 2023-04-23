package com.example.smartsavr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChildSummaryActivity extends AppCompatActivity {
    static final long CENTS = 100;
    static List<Chore> listApprovedChores = new ArrayList<>();
    static List<String> xAxisBottomLabels = new ArrayList<>();
    ArrayList<Entry> values = new ArrayList<>();
    TextView weekly_earnings;
    TextView monthly_earnings;
    ChildChartData child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_summary);

        Intent intent = getIntent();
        String childId = intent.getStringExtra(Utils.CHILD_ID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(childId + "'s Earning Summary");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        child = new ChildChartData(childId);
        child.setChoresList(chores -> {
            listApprovedChores = child.getListApprovedChores();
            values = child.getValues();
            xAxisBottomLabels = child.getXAxisLabels();
            populateGraph();

            //Set earning stats
            weekly_earnings = findViewById(R.id.weekly_amt);
            weekly_earnings.setText(String.format("%s%s", getString(R.string.weekly_earning), calculateWeeklyEarnings()));
            monthly_earnings = findViewById(R.id.monthly_amt);
            monthly_earnings.setText(String.format("%s%s", getString(R.string.monthly_earning), calculateMonthlyEarnings()));
        });
    }

    public void populateGraph() {
        LineChart lineChart = findViewById(R.id.barChart);

        if (values.size() == 0) {
            lineChart.setData(null);
            lineChart.setNoDataText("There is currently no graph data to display.");
            lineChart.invalidate();
            return;
        }

        //configure graph settings
        XAxis XAxisBottom = lineChart.getXAxis();
        XAxisBottom.setPosition(XAxis.XAxisPosition.TOP);
        XAxisBottom.setGranularityEnabled(true);
        XAxisBottom.setGranularity(1f);
        XAxisBottom.setLabelCount(values.size(), false);
        XAxisBottom.setValueFormatter(new IndexAxisValueFormatter(xAxisBottomLabels));


        YAxis yAxis = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxis.setAxisMinimum(0f);
        yAxisRight.setAxisMinimum(0f);

        LineDataSet dataset = new LineDataSet(values,"Earnings");
        dataset.setLineWidth(2.5f);
        dataset.setCircleRadius(4.5f);
        dataset.setHighLightColor(Color.rgb(244, 117, 117));
        dataset.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        dataset.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        dataset.setDrawValues(true);
        dataset.setValueTextSize(11);
        LineData data = new LineData(dataset);
        lineChart.setData(data);
        lineChart.setHorizontalScrollBarEnabled(true);
        lineChart.setVisibleXRange(7,7);
        lineChart.moveViewToX(values.get(values.size()-1).getX());
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.invalidate();
    }


    public float calculateWeeklyEarnings() {
        long ts = System.currentTimeMillis();
        float earnings = 0;
        for (Chore chore : listApprovedChores) {
            if (checkDaysOfSameWeek(ts, chore.getCompletedTimestamp())) {
                earnings += chore.getRewardCents();
            }
        }
        return earnings/CENTS;
    }

    public float calculateMonthlyEarnings() {
        long ts = System.currentTimeMillis();
        float earnings = 0;
        for (Chore chore : listApprovedChores) {
            if (checkDaysOfSameMonth(ts, chore.getCompletedTimestamp())) {
                earnings += chore.getRewardCents();
            }
        }
        return earnings/CENTS;
    }

    public boolean checkDaysOfSameWeek(long timestamp1, long timestamp2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(timestamp1);
        int cal1_year = cal1.get(Calendar.YEAR);
        int cal1_week = cal1.get(Calendar.WEEK_OF_YEAR);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(timestamp2);
        int cal2_year = cal2.get(Calendar.YEAR);
        int cal2_week = cal2.get(Calendar.WEEK_OF_YEAR);

        return (cal1_year == cal2_year && cal1_week == cal2_week);
    }

    public boolean checkDaysOfSameMonth(long timestamp1, long timestamp2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(timestamp1);
        int cal1_year = cal1.get(Calendar.YEAR);
        int cal1_month = cal1.get(Calendar.MONTH);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(timestamp2);
        int cal2_year = cal2.get(Calendar.YEAR);
        int cal2_month = cal2.get(Calendar.MONTH);

        return (cal1_year == cal2_year && cal1_month == cal2_month);
    }
}
