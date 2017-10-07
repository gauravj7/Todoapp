package com.example.gaurav.todoapp;

/**
 * Created by GAURAV on 9/27/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ralph on 16/09/17.
 */

public class ExpenseOpenHelper extends SQLiteOpenHelper {


    private static ExpenseOpenHelper instance;


    public static ExpenseOpenHelper getInstance(Context context) {
        if(instance == null){
            instance = new ExpenseOpenHelper(context);
        }
        return instance;
    }

    private ExpenseOpenHelper(Context context) {
        super(context, "expense_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + Contract.EXPENSE_TABLE_NAME + " ( " +
                Contract.EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.EXPENSE_TITLE + " TEXT, " +
                Contract.DATE + " TEXT, " +
                Contract.TIME + " TEXT, " +
                Contract.ALARMTIME + " INTEGER)";


        sqLiteDatabase.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
