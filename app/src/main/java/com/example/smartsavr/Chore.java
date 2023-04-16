package com.example.smartsavr;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Chore {

    @DocumentId
    private String id;
    private String childID;
    private long assignedTimestamp;
    private long deadline;
    private String taskName;
    private int rewardCents;
    private boolean isComplete;

    private boolean isApproved;
    private long completedTimestamp;

    private long approvedTimestamp;

    public Chore( String childID, long deadline, String taskName, int rewardCents) {
        this.childID = childID;
        this.assignedTimestamp = System.currentTimeMillis();
        this.deadline = deadline;
        this.taskName = taskName;
        this.rewardCents = rewardCents;
        this.isComplete = false;
        this.completedTimestamp = Integer.MAX_VALUE;
        this.isApproved = false;
        this.approvedTimestamp = Integer.MAX_VALUE;

    }

    public Chore(){

    }






    public String getChildID() {
        return childID;
    }

    public void setChildID(String childID) {
        this.childID = childID;
    }

    public long getAssignedTimestamp() {
        return assignedTimestamp;
    }

    public void setAssignedTimestamp(long assignedTimestamp) {
        this.assignedTimestamp = assignedTimestamp;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getRewardCents() {
        return rewardCents;
    }

    public void setRewardCents(int rewardCents) {
        this.rewardCents = rewardCents;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public long getCompletedTimestamp() {
        return completedTimestamp;
    }

    public void setCompletedTimestamp(long completedTimestamp) {
        this.completedTimestamp = completedTimestamp;
    }

    @Override
    public String toString(){
        return this.taskName;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public long getApprovedTimestamp() {
        return approvedTimestamp;
    }

    public void setApprovedTimestamp(long approvedTimestamp) {
        this.approvedTimestamp = approvedTimestamp;
    }
}
