package com.example.smartsavr;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    final String TAG = "FCMService";
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;

    public MyFirebaseMessagingService() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.collectionReference = firebaseFirestore.collection("chores");
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        getFirebaseMessage(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    public void getFirebaseMessage(String title, String msg) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setAutoCancel(true);


        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    public void checkToSendAddChoreNotification(String childId) {
        collectionReference = firebaseFirestore.collection("chores");
        Query queryChoreLastAdded = collectionReference.whereEqualTo("childID", childId).orderBy("assignedTimestamp", Query.Direction.DESCENDING);
        queryChoreLastAdded.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                System.err.println("Listen failed:" + error);
                return;
            }

            if (snapshots != null && snapshots.getDocuments().get(0) != null) {
                Chore lastAddedChore = snapshots.getDocuments().get(0).toObject(Chore.class);
                if (lastAddedChore != null && lastAddedChore.getAssignedTimestamp() > System.currentTimeMillis() - 10000) {
                    sendChildNotification(lastAddedChore, childId, "addChore");
                }
            }
        });
    }

    public void checkToSendApprovedChoreNotification(String childId) {
        collectionReference = firebaseFirestore.collection("chores");
        Query queryChoreLastApproved = collectionReference.whereEqualTo("childID", childId).whereEqualTo("approved", true).orderBy("approvedTimestamp", Query.Direction.DESCENDING);
        queryChoreLastApproved.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                System.err.println("Listen failed:" + error);
                return;
            }

            if (snapshots != null && snapshots.getDocuments().get(0) != null) {
                Chore lastApprovedChore = snapshots.getDocuments().get(0).toObject(Chore.class);
                if (lastApprovedChore != null && lastApprovedChore.getApprovedTimestamp() > System.currentTimeMillis() - 10000) {
                    sendChildNotification(lastApprovedChore, childId, "approveChore");
                }
            }
        });
    }



    public void sendChildNotification(Chore chore, String childId, String notificationType) {
        subscribeToNotifications(childId);
        new Thread(() -> {
            JSONObject jPayload = new JSONObject();
            JSONObject jNotification = new JSONObject();
            try {
                switch(notificationType) {
                    case ("addChore"):
                        jNotification.put("title", "Chore " + chore.getTaskName() + " has been added or edited!");
                        jNotification.put("body", "View your chores list in the app for more details.");
                        break;
                    case ("approveChore"):
                        if (chore.getRewardCents() != 0) {
                            jNotification.put("title", "You've earned " + Utils.centsToDollarString(chore.getRewardCents(),true) + " from newly approved chore " + chore.getTaskName() + "!");
                            jNotification.put("body", "Great job on completing the task! View account balance change in the app for more details.");
                        } else {
                            jNotification.put("title", "Chore " + chore.getTaskName() + " has been approved!");
                            jNotification.put("body", "Great job on completing the task! View your chores list in the app for more details.");
                        }
                        break;
                }
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

    public void checkNotificationPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    public void subscribeToNotifications(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                    String msg = "Done";
                    if (!task.isSuccessful()) {
                        msg = "Failed";
                    }
                    Log.d(TAG, "Subscribe to notifications " + msg);
                });
    }

}
