package com.example.smartsavr;

import java.io.Serializable;

public class Child implements Serializable {

    private String name,parent_id,password,username;
    private int account_balance;
    private int weekly_allowance;

    private int profilePictureID;
    private int choresCompleted;

    // as currently account balance and password is not implemented
    public Child(String name, String parent_id,int weekly_allowance,String username,String password,int account_balance, int profilePictureID, int choresCompleted) {

        this.name = name;
        this.parent_id = parent_id;
        this.weekly_allowance = weekly_allowance;
        this.username = username;
        this.password = password;
        this.account_balance=account_balance;
        this.profilePictureID = profilePictureID;
        this.choresCompleted = choresCompleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parent_id;
    }

    public void setParentId(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountBalance() {
        return account_balance;
    }

    public void setAccountBalance(int account_balance) {
        this.account_balance = account_balance;
    }

    public int getWeeklyAllowance() {
        return weekly_allowance;
    }

    public void setWeeklyAllowance(int weekly_allowance) {
        this.weekly_allowance = weekly_allowance;
    }

    public void setUsername(String username)
    {
        this.username=username;
    }

    public String getUsername(){
        return username;
    }

    public int getProfilePicture() {
        return profilePictureID;
    }

    public void setProfilePicture(int profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    public int getChoresCompleted() {
        return choresCompleted;
    }

    public void setChoresCompleted(int choresCompleted) {
        this.choresCompleted = choresCompleted;
    }

}
