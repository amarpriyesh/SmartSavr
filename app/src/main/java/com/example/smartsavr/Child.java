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

    // as currently account balance and password is not implemented
    public Child(String child_id, String name, String parent_id, String password, int weekly_allowance) {
        this.child_id = child_id;
        this.name = name;
        this.parent_id = parent_id;
        this.password = password;

        this.weekly_allowance = weekly_allowance;
    }

    public Child(String child_id, String name, String parent_id, int weekly_allowance) {
        this.child_id = child_id;
        this.name = name;
        this.parent_id = parent_id;
        this.password = password;

        this.weekly_allowance = weekly_allowance;
    }












}
