package com.example.gaurav.todoapp;

/**
 * Created by GAURAV on 9/27/2017.
 */

import java.io.Serializable;


public class Expense implements Serializable{

    private String title;
   // private String amount;
    private String date;
    private String time;
    private int id;

    public Expense(String title,String date,String time,int id) {
        this.title = title;
        //this.amount = amount;
        this.date=date;
        this.time=time;
        this.id = id;
    }

//    public Expense(String title, int id) {
//        this.title = title;
//        //this.amount = amount;
//        this.id = id;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getdate() {
        return date;
    }

    public void setdate(String title) {
        this.date = date;
    }
    public String gettime() {
        return time;
    }

    public void settime(String title) {
        this.time = time;
    }

//    public String getAmount() {
//        return amount;
//    }

//    public void setAmount(int amount) {
//        this.amount = amount;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

