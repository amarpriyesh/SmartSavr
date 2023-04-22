package com.example.smartsavr;

import static com.example.smartsavr.Utils.centsToDollarString;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.smartsavr.databinding.ActivityChildHomeBinding;

import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildHomeActivity extends AppCompatActivity {


    static List<Chore> listChoresCompleted =new ArrayList<>();
    static List<Chore> listChoresToDo = new ArrayList<>();

    static DBReference choresCompletedDBReference;
    static DBReference toDoCompletedDBReference;

    ChoresPoller poller;

    final String TAG = "ChildHomeActivity";
    ActivityChildHomeBinding binding;

    CalendarOperation cal;

    ChoresListFragment completedActivityFragmnet;
    ChoresListFragment toDoActivityFragmnet;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;

    static String childId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_home);
        }

        Intent intent = getIntent();
        childId = intent.getStringExtra(Utils.CHILD_ID);
        Log.d(TAG, String.format("Child ID is %s", childId));

        listChoresCompleted.clear();
        listChoresToDo.clear();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("chores");


        choresCompletedDBReference = new DBReference(collectionReference, firebaseFirestore);
        toDoCompletedDBReference = new DBReference(collectionReference, firebaseFirestore);

        Query queryChoresCompleted = collectionReference.whereEqualTo("childID", childId).whereEqualTo("complete", true).orderBy("completedTimestamp", Query.Direction.DESCENDING).limit(3);
        Query queryChoresApproved = collectionReference.whereEqualTo("childID", childId).whereEqualTo("complete", true).whereEqualTo("approved", true);

        choresCompletedDBReference.setQuery(queryChoresCompleted);
        choresCompletedDBReference.setQueryComplete(queryChoresApproved);
        choresCompletedDBReference.setApprovedListener((total, sumWeekly, sumMonthly) -> {
            binding.textTotal.setText(centsToDollarString(total));
            binding.textWeekly.setText(getResources().getString(R.string.weekly_earnings, centsToDollarString(sumWeekly)));
            binding.textMonthly.setText(getResources().getString(R.string.monthly_earnings, centsToDollarString(sumMonthly)));
        });
        Query queryChoresToDo = collectionReference.whereEqualTo("childID", childId).whereEqualTo("complete", false).orderBy("deadline", Query.Direction.ASCENDING).limit(3);
        toDoCompletedDBReference.setQuery(queryChoresToDo);

        completedActivityFragmnet = ChoresListFragment.newInstance("childChoresCompleted");
        toDoActivityFragmnet = ChoresListFragment.newInstance("childChoresToDo");
        setFragment(R.id.fragmentCompletedActivities, completedActivityFragmnet);
        setFragment(R.id.fragmentUpcomingActivities, toDoActivityFragmnet);
        setListeners();
        createNotificationChannel();
        checkToSendNotification();
    }

    private void setFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    private void setListeners(){
        binding.linkCompletedActivities.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChildHomeChoresCompleted.class);
            startActivity(intent);
        });
        binding.linkUpcomingActivities.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChildHomeChoresUpcoming.class);
            startActivity(intent);
        });
        //TODO For Graphs
        binding.linkSeeGraphs.setOnClickListener(v -> {});
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            myIntent.putExtra(SettingsActivity.IS_CHILD_USER, true);
            startActivity(myIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    public void checkNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    // Create notification channel and subscribe user to their channel
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel
                (getString(R.string.channel_id), childId, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Notifications for "+ childId);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        subscribeToNotifications();
        checkNotificationPermissions();
    }

    public void subscribeToNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic(childId)
                .addOnCompleteListener(task -> {
                    String msg = "Done";
                    if (!task.isSuccessful()) {
                        msg = "Failed";
                    }
                    Log.d(TAG, "Subscribe to notifications " + msg);
                });
    }

    public void checkToSendNotification() {
        Query queryChoreLastAdded = collectionReference.whereEqualTo("childID", childId).orderBy("assignedTimestamp", Query.Direction.DESCENDING);
        queryChoreLastAdded.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                System.err.println("Listen failed:" + error);
                return;
            }

            if (snapshots != null && snapshots.getDocuments().get(0) != null) {
                Chore lastAddedChore = snapshots.getDocuments().get(0).toObject(Chore.class);
                if (lastAddedChore != null && lastAddedChore.getAssignedTimestamp() > System.currentTimeMillis() - 10000) {
                    sendAddChoreNotification(lastAddedChore);
                }
            }
        });
    }

    public void sendAddChoreNotification(Chore chore) {
        new Thread(() -> {
            JSONObject jPayload = new JSONObject();
            JSONObject jNotification = new JSONObject();
            try {
                jNotification.put("title", "Chore " + chore.getTaskName() + " has been added or edited!");
                jNotification.put("body", "View in the app for more details.");

                // Populate the Payload object with our notification information
                // sent to topic of the user we're sending to
                jPayload.put("to", "/topics/" + childId);
                jPayload.put("notification", jNotification);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String messageResponse = Utils.fcmHttpConnection(jPayload);
            Log.d(TAG, "notification sent to" + childId);
            Log.d(TAG, messageResponse);
        }).start();
    }
}

