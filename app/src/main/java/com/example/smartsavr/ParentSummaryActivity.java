package com.example.smartsavr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ParentSummaryActivity extends AppCompatActivity {

    ArrayList<String> children = new ArrayList<>();

    ParentChartData parentChartData;
    private CombinedChart combinedChart;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_summary);

        Intent intent = getIntent();
        String parentId = (String) intent.getStringExtra(Utils.PARENT_ID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Allowance Summary");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        parentChartData = new ParentChartData(parentId);
        parentChartData.setChartData(new ParentChartData.DBCallback() {
            @Override
            public void onCallback(ArrayList<String> children_names) {
                if (children_names.size() == 0) {
                    combinedChart.setData(null);
                    combinedChart.setNoDataText("There is currently no graph data to display.");
                    combinedChart.invalidate();
                    pieChart.setData(null);
                    pieChart.setNoDataText("There is currently no graph data to display.");
                    pieChart.invalidate();
                } else {
                    children = parentChartData.getChildrenNames();
                    populateLineBarGraph(parentChartData.getBalanceBar(), parentChartData.getAllowance());
                    populatePieBarGraph(parentChartData.getBalancePie());
                }
            }

            public void populateLineBarGraph(ArrayList<BarEntry> barEntryList, ArrayList<Entry> lineEntryList) {
                combinedChart = findViewById(R.id.lineBarChartParent);
                combinedChart.getDescription().setEnabled(false);
                combinedChart.setBackgroundColor(Color.WHITE);
                combinedChart.setDrawGridBackground(false);
                combinedChart.setDrawBarShadow(false);
                combinedChart.setHighlightFullBarEnabled(false);
                combinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                        CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});

                LineData lineData = new LineData();
                LineDataSet line_dataset = new LineDataSet(lineEntryList, "Line DataSet");
                line_dataset.setColor(Color.rgb(240, 238, 70));
                line_dataset.setLineWidth(2.5f);
                line_dataset.setCircleColor(Color.rgb(240, 238, 70));
                line_dataset.setCircleRadius(5f);
                line_dataset.setFillColor(Color.rgb(240, 238, 70));
                line_dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                line_dataset.setDrawValues(true);
                line_dataset.setValueTextSize(10f);
                line_dataset.setValueTextColor(Color.rgb(240, 238, 70));
                line_dataset.setAxisDependency(YAxis.AxisDependency.LEFT);
                line_dataset.setLabel("Weekly Allowance");
                line_dataset.setValueTextSize(12f);
                lineData.addDataSet(line_dataset);

                BarData barData = new BarData();
                BarDataSet bar_dataset = new BarDataSet(barEntryList, "Bar Dataset");
                bar_dataset.setColors(ColorTemplate.MATERIAL_COLORS);
                bar_dataset.setValueTextColor(Color.rgb(60, 220, 78));
                bar_dataset.setValueTextSize(10f);
                bar_dataset.setAxisDependency(YAxis.AxisDependency.LEFT);
                bar_dataset.setLabel("Account Balance");
                bar_dataset.setValueTextSize(12f);
                barData.addDataSet(bar_dataset);

                YAxis rightAxis = combinedChart.getAxisRight();
                rightAxis.setDrawGridLines(false);
                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
                rightAxis.setTextSize(11f);

                YAxis leftAxis = combinedChart.getAxisLeft();
                leftAxis.setDrawGridLines(false);
                leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
                leftAxis.setTextSize(11f);

                XAxis xAxis = combinedChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1f);
                xAxis.setAxisMinimum(-.5f);
                xAxis.setAxisMaximum(bar_dataset.getXMax() + .5f);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(children));
                xAxis.setTextSize(12f);

                CombinedData data = new CombinedData();
                data.setData(barData);
                data.setData(lineData);
                combinedChart.setData(data);
                combinedChart.invalidate();
            }

            public void populatePieBarGraph(ArrayList<PieEntry> pieEntryList) {
                pieChart = findViewById(R.id.pieChartParent);
                PieDataSet dataSet = new PieDataSet(pieEntryList, "");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);

                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                //dataSet.setSelectionShift(0f);

                PieData data = new PieData(dataSet);
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);

                float sum = 0;
                for (PieEntry entry : pieEntryList) {
                    sum += entry.getValue();
                }
                pieChart.setCenterText("Total Balance: \n $" + sum);

                pieChart.setBackgroundColor(Color.WHITE);
                pieChart.getDescription().setEnabled(false);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleColor(Color.WHITE);
                pieChart.setTransparentCircleColor(Color.WHITE);
                pieChart.setTransparentCircleAlpha(110);
                pieChart.setHoleRadius(58f);
                pieChart.setTransparentCircleRadius(61f);
                pieChart.setDrawCenterText(true);
                pieChart.setData(data);
                pieChart.animateY(1400, Easing.EaseInOutQuad);
                Legend l = pieChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                pieChart.invalidate();
            }

        });




    }
}