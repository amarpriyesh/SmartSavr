package com.example.smartsavr;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class Child implements Serializable {

    private static final int MAX_ACCOUNT_BALANCE_CENTS = 1000000;

    @DocumentId
    private String id;
    private String name;
    private String parentId;
    private String password;
    private String username;
    private int accountBalanceCents;
    private int weeklyAllowanceCents;
    private int profilePicture;
    private int choresCompleted;

    private long lastAllowanceTime;

    //Constructor for a new child
    public Child(String name, String parentId, int weeklyAllowanceCents, String username, String password, int accountBalanceCents, int profilePicture, int choresCompleted) {
        this.name = name;
        this.parentId = parentId;
        this.weeklyAllowanceCents = weeklyAllowanceCents;
        this.username = username;
        this.password = password;
        this.accountBalanceCents = accountBalanceCents;
        this.profilePicture = profilePicture;
        this.choresCompleted = choresCompleted;
        //With a new child, the last allowance time is set to the previous Sunday
        LocalDateTime localDate = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));
        ZonedDateTime zonedDateTime = localDate.atZone(ZoneId.of("America/New_York"));
        this.lastAllowanceTime = zonedDateTime.toInstant().toEpochMilli();
    }

    //Constructor for a child that already exists in the db
    public Child(String name, String parentId, int weeklyAllowanceCents, String username, String password, int accountBalanceCents, int profilePicture, int choresCompleted, long lastAllowanceTime) {
        this.name = name;
        this.parentId = parentId;
        this.weeklyAllowanceCents = weeklyAllowanceCents;
        this.username = username;
        this.password = password;
        this.accountBalanceCents = accountBalanceCents;
        this.profilePicture = profilePicture;
        this.choresCompleted = choresCompleted;
        this.lastAllowanceTime = lastAllowanceTime;
    }

    public Child() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountBalanceCents() {
        return accountBalanceCents;
    }

    public void setAccountBalanceCents(int accountBalanceCents) {
        this.accountBalanceCents = accountBalanceCents;

        if (this.accountBalanceCents < 0) {
            this.accountBalanceCents = 0;
        }

        if (this.accountBalanceCents > MAX_ACCOUNT_BALANCE_CENTS) {
            this.accountBalanceCents = MAX_ACCOUNT_BALANCE_CENTS;
        }
    }

    public int getWeeklyAllowanceCents() {
        return weeklyAllowanceCents;
    }

    public void setWeeklyAllowanceCents(int weeklyAllowanceCents) {
        this.weeklyAllowanceCents = weeklyAllowanceCents;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getChoresCompleted() {
        return choresCompleted;
    }

    public void setChoresCompleted(int choresCompleted) {
        this.choresCompleted = choresCompleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastAllowanceTime() {
        return lastAllowanceTime;
    }

    public void setLastAllowanceTime(long lastAllowanceTime) {
        this.lastAllowanceTime = lastAllowanceTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "Child{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", accountBalanceCents=" + accountBalanceCents +
                ", weeklyAllowanceCents=" + weeklyAllowanceCents +
                ", profilePicture=" + profilePicture +
                ", choresCompleted=" + choresCompleted +
                ", lastAllowanceTime=" + lastAllowanceTime +
                '}';
    }
}
