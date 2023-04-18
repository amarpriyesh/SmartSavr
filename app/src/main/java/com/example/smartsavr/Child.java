package com.example.smartsavr;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Child implements Serializable {

    private String name;
    private String parentId;
    private String password;
    private String username;
    private int accountBalance;
    private int weeklyAllowance;
    private int profilePicture;
    private int choresCompleted;

    // as currently account balance and password is not implemented
    public Child(String name, String parentId, int weeklyAllowance, String username, String password, int accountBalance, int profilePicture, int choresCompleted) {
        this.name = name;
        this.parentId = parentId;
        this.weeklyAllowance = weeklyAllowance;
        this.username = username;
        this.password = password;
        this.accountBalance = accountBalance;
        this.profilePicture = profilePicture;
        this.choresCompleted = choresCompleted;
    }

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

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public int getWeeklyAllowance() {
        return weeklyAllowance;
    }

    public void setWeeklyAllowance(int weeklyAllowance) {
        this.weeklyAllowance = weeklyAllowance;
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

    @NonNull
    @Override
    public String toString() {
        return "Child{" +
                "name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", accountBalance=" + accountBalance +
                ", weeklyAllowance=" + weeklyAllowance +
                ", profilePicture=" + profilePicture +
                ", choresCompleted=" + choresCompleted +
                '}';
    }
}
