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

import java.util.Objects;

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
        getFirebaseMessage(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), remoteMessage.getNotification().getBody());
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
                Log.w(TAG, "Listen failed.", error);
                return;
            }

            if (snapshots != null && snapshots.size() != 0) {
                Chore lastAddedChore = snapshots.getDocuments().get(0).toObject(Chore.class);
                if (lastAddedChore != null && lastAddedChore.getAssignedTimestamp() > System.currentTimeMillis() - 10000) {
                    //Check if the data change is actually because of add/edit chore
                    if (lastAddedChore.getAssignedTimestamp() > lastAddedChore.getApprovedTimestamp() && lastAddedChore.getAssignedTimestamp() > lastAddedChore.getCompletedTimestamp()) {
                        sendChildNotification(lastAddedChore, childId, "addChore");
                    }
                }
            }
        });
    }

    public void checkToSendApprovedChoreNotification(String childId) {
        collectionReference = firebaseFirestore.collection("chores");
        Query queryChoreLastApproved = collectionReference.whereEqualTo("childID", childId).whereEqualTo("approved", true).orderBy("approvedTimestamp", Query.Direction.DESCENDING);
        queryChoreLastApproved.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }

            if (snapshots != null && snapshots.size() != 0) {
                Chore lastApprovedChore = snapshots.getDocuments().get(0).toObject(Chore.class);
                if (lastApprovedChore != null && lastApprovedChore.getApprovedTimestamp() > System.currentTimeMillis() - 10000) {
                    //Check if the data change is actually because of approve chore
                    if (lastApprovedChore.getApprovedTimestamp() > lastApprovedChore.getAssignedTimestamp() && lastApprovedChore.getApprovedTimestamp() > lastApprovedChore.getCompletedTimestamp()) {
                        sendChildNotification(lastApprovedChore, childId, "approveChore");
                    }
                }
            }
        });
    }

    public void checkToSendCompletedChoreNotification(String parentId, Child child) {
        collectionReference = firebaseFirestore.collection("chores");
        Query queryChoreLastApproved = collectionReference.whereEqualTo("childID", child.getId()).whereEqualTo("complete", true).orderBy("completedTimestamp", Query.Direction.DESCENDING);
        queryChoreLastApproved.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }

            if (snapshots != null && snapshots.size() != 0) {
                Chore lastCompletedChore = snapshots.getDocuments().get(0).toObject(Chore.class);
                if (Objects.requireNonNull(lastCompletedChore).getCompletedTimestamp() > System.currentTimeMillis() - 10000) {
                    //Check if the data change is actually because of complete chore
                    if (lastCompletedChore.getCompletedTimestamp() > lastCompletedChore.getAssignedTimestamp() && lastCompletedChore.getCompletedTimestamp() > lastCompletedChore.getApprovedTimestamp()) {
                        sendParentNotification(parentId, child);
                    }
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
                    case "addChore":
                        jNotification.put("title", String.format("Chore %s has been added or edited!", chore.getTaskName()));
                        jNotification.put("body", "View your chores list in the app for more details.");
                        break;
                    case ("approveChore"):
                        if (chore.getRewardCents() != 0) {
                            jNotification.put("title", String.format("You've earned %s from newly approved chore!", Utils.centsToDollarString(chore.getRewardCents(), true)));
                            jNotification.put("body", "Great job on completing the task! View in the app for more details.");
                        } else {
                            jNotification.put("title", String.format("Chore %s has been approved!", chore.getTaskName()));
                            jNotification.put("body", "Great job on completing the task! View in the app for more details.");
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

    public void sendParentNotification(String parentId, Child child) {
        subscribeToNotifications(String.format("%s%s", parentId.charAt(0), child.getId()));
        new Thread(() -> {
            JSONObject jPayload = new JSONObject();
            JSONObject jNotification = new JSONObject();
            try {
                jNotification.put("title", String.format("%s has completed a chore!", child.getName()));
                jNotification.put("body", "View in the app to approve the completed chore.");
                jPayload.put("to", "/topics/" + String.format("%s%s", parentId.charAt(0), child.getId()));
                jPayload.put("notification", jNotification);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String messageResponse = Utils.fcmHttpConnection(jPayload);
            Log.d(TAG, "notification sent to" + String.format("%s%s", parentId.charAt(0), child.getId()));
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
