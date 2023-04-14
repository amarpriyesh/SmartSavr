package com.example.smartsavr;

public class Chore {
    private String uuid;
    private String childID;
    private long assignedTimestamp;
    private long deadline;
    private String taskName;
    private int rewardCents;
    private boolean isComplete;
    private long completedTimestamp;

    public Chore(String uuid, String childID, long assignedTimestamp, long deadline, String taskName, int rewardCents, boolean isComplete, long completedTimestamp) {
        this.uuid = uuid;
        this.childID = childID;
        this.assignedTimestamp = assignedTimestamp;
        this.deadline = deadline;
        this.taskName = taskName;
        this.rewardCents = rewardCents;
        this.isComplete = isComplete;
        this.completedTimestamp = completedTimestamp;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
