package com.example.myapplication.Bean;

import java.util.Date;

public class User {
    private int user_id;
    private String username;
    private int account_money;
    private int bond;
    private Date create_time;
    //余额充值数目
    private int money;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccount_money() {
        return account_money;
    }

    public void setAccount_money(int account_money) {
        this.account_money = account_money;
    }

    public int getBond() {
        return bond;
    }

    public void setBond(int bond) {
        this.bond = bond;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
