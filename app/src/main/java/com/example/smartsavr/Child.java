package com.example.smartsavr;

public class Child {

    private String name,parent_id,password,username;
    private int account_balance;
    private int weekly_allowance;

    public Child(){

    }



    // as currently account balance and password is not implemented
    public Child(String name, String parent_id,int weekly_allowance,String username,String password,int account_balance) {

        this.name = name;
        this.parent_id = parent_id;
        this.weekly_allowance = weekly_allowance;
        this.username = username;
        this.password = password;
        this.account_balance=account_balance;
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


    public void setUsername(String username)
    {
        this.username=username;
    }

    public String getusername(){
        return username;
    }
}
