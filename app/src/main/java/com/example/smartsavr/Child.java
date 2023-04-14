package com.example.smartsavr;

public class Child {

    private String child_id,name,parent_id,password;
    private int account_balance;
    private int weekly_allowance;

    public Child(){

    }

    public Child(String child_id, String name, String parent_id, String password, int account_balance, int weekly_allowance) {
        this.child_id = child_id;
        this.name = name;
        this.parent_id = parent_id;
        this.password = password;
        this.account_balance = account_balance;
        this.weekly_allowance = weekly_allowance;
    }


    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(int account_balance) {
        this.account_balance = account_balance;
    }

    public int getWeekly_allowance() {
        return weekly_allowance;
    }

    public void setWeekly_allowance(int weekly_allowance) {
        this.weekly_allowance = weekly_allowance;
    }
}
