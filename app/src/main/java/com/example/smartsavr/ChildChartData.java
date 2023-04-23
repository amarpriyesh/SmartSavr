package com.example.smartsavr;

import android.util.Log;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ChildChartData {
    static final long DAY_INCREMENT = 1000 * 60 * 60 * 24;
    static final long CENTS = 100;

    static final long startingAxisTimestamp = System.currentTimeMillis() - (30 * DAY_INCREMENT); // a month ago
    private List<Chore> listApprovedChores = new ArrayList<>();
    private final List<String> xAxisBottomLabels = new ArrayList<>();
    static ArrayList<Entry> values = new ArrayList<>();
    private float xValue = 0;
    FirebaseFirestore db;
    CollectionReference chores;
    private final String childId;
    private final String TAG = "DB ERROR";

    public ChildChartData(String childId) {
        this.childId = childId;
    }

    public List<Chore> getListApprovedChores() {
        return listApprovedChores;
    }

    public void setChoresList(DBCallback dbCallback) {
        listApprovedChores.clear();

        db = FirebaseFirestore.getInstance();
        chores = db.collection("chores");

        //TODO change query back to correct conditions
        Query query = chores.whereEqualTo("childID",childId).whereEqualTo("complete",true).orderBy("completedTimestamp", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (QueryDocumentSnapshot document : snapshot) {
                    Chore chore = document.toObject(Chore.class);
                    listApprovedChores.add(chore);
                    calculateDailyEarnings();
                    populateXAxisLabels();
                }
                dbCallback.onCallback(listApprovedChores);
            } else {
                listApprovedChores = null;
                Log.e(TAG, "Database error when loading documents");
            }
        });
    }

    public long getLastDayTimestamp() {
        return listApprovedChores.get(listApprovedChores.size()-1).getCompletedTimestamp();
    }

    public ArrayList<Entry> getValues() {
        return values;
    }

    public List<String> getXAxisLabels() {
        return xAxisBottomLabels;
    }

    public void calculateDailyEarnings() {
        values.clear();
        if (listApprovedChores.size() == 0) {
            populateDataBetweenDateGaps(startingAxisTimestamp, System.currentTimeMillis());
        } else {
            int earning = 0;
            long compareToTimestamp = startingAxisTimestamp;
            for (Chore chore : listApprovedChores) {
                long choreTimestamp = chore.getCompletedTimestamp();
                //if same day timestamp, reward gets added to that day's earnings
                if (compareTwoDates(compareToTimestamp, choreTimestamp) == 0) {
                    earning += chore.getRewardCents();
                } else {
                    //add previous day's records to chart data
                    values.add(new Entry(xValue, (float) earning / CENTS));
                    xValue++;
                    if (compareTwoDates(compareToTimestamp, choreTimestamp) >= 2) {
                        //populate data gap entries
                        populateDataBetweenDateGaps(compareToTimestamp, choreTimestamp);
                    }
                    //set compareToDate to next date and reset earnings amt
                    compareToTimestamp = choreTimestamp;
                    earning = chore.getRewardCents();
                }
            }
            //add last day's records to chart data
            values.add(new Entry(xValue, (float) earning / 100));
            xValue++;
            populateDataBetweenDateGaps(getLastDayTimestamp(), System.currentTimeMillis());
        }
    }

    public String convertTimestampToDate(long timestamp) {
        SimpleDateFormat date = new SimpleDateFormat("MM-dd", Locale.US);
        date.setTimeZone(getTimeZone());
        return date.format(timestamp);
    }

    private TimeZone getTimeZone() {
        return TimeZone.getTimeZone("America/New_York");
    }

    public int compareTwoDates(long timestamp1, long timestamp2) {
        Calendar date1 = getCalendar(timestamp1);
        Calendar date2 = getCalendar(timestamp2);
        return date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR);
    }

    @NonNull
    private Calendar getCalendar(long timestamp) {
        Calendar date = Calendar.getInstance();
        date.setTimeZone(getTimeZone());
        date.setTimeInMillis(timestamp);
        return date;
    }

    public void populateDataBetweenDateGaps(long compareToTimestamp, long choreTimestamp) {
        int i = 1;
        int gap_days = compareTwoDates(compareToTimestamp, choreTimestamp);
        while (i < gap_days) {
            values.add(new Entry(xValue, 0f));
            i++;
            xValue++;
        }
    }

    public void populateXAxisLabels() {
        xAxisBottomLabels.clear();
        int i = 0;
        int gap_days = compareTwoDates(startingAxisTimestamp, System.currentTimeMillis());
        long nextDayTimestamp = startingAxisTimestamp + DAY_INCREMENT;
        while (i < gap_days) {
            xAxisBottomLabels.add(convertTimestampToDate(nextDayTimestamp));
            i++;
            nextDayTimestamp += DAY_INCREMENT;
        }
    }

    interface DBCallback {
        void onCallback(List<Chore> chores);
    }

}
