package com.example.gaurav.todoapp;

/**
 * Created by GAURAV on 9/27/2017.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;

import android.view.Menu;

import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddExpenseActivity extends AppCompatActivity {
    Button button_stpd;
    static final int Dialog_Id = 0;
    private int hour_x;
    private int minute_x;
    private TimePicker timePicker1;
    private DatePicker datePicker;
    private TextView dateView;
    private int year, month, day;
    private TextView time;
    private String format = "";
    private TextView timeview;
    Calendar calendar;
//    BroadcastReceiver broadcastReceiver;
//    LocalBroadcastManager broadcastManager;
    Calendar mainCalendar = Calendar.getInstance();


    public static final int ADD_SUCCESS = 1;

    EditText title;
    EditText amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        dateView = (TextView) findViewById(R.id.date);
        timeview = (TextView)findViewById(R.id.timeview);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

//        IntentFilter intentFilter = new IntentFilter(MY_ACTION);
//        broadcastManager.registerReceiver(broadcastReceiver,intentFilter);

        title = (EditText) findViewById(R.id.titleEdit);
//        amount = (EditText) findViewById(R.id.amountEdit);
        button_stpd = (Button) findViewById(R.id.time);
        button_stpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialog_Id);
            }

        });
//        broadcastManager = LocalBroadcastManager.getInstance(this);
//
//
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Toast.makeText(AddExpenseActivity.this,"Custom Broadcast",Toast.LENGTH_SHORT).show();
//            }
//        };
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
            if(id==Dialog_Id)
            {
                return new TimePickerDialog(AddExpenseActivity.this,kTimePickerListener,hour_x,minute_x,false);
            }
        return null;
    }

   // Calendar mainCalendar = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    mainCalendar.set(arg1, arg2, arg3, 0, 0, 0);
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

  private void showtime(int hour, int minute)
  {
      timeview.setText(hour + " : " + minute);
  }



    protected TimePickerDialog.OnTimeSetListener kTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x =hourOfDay;
            minute_x = minute;
            mainCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mainCalendar.set(Calendar.MINUTE, minute);
            showtime(hourOfDay,minute);

        }
    };

    public void add(View view){
        String  todotitle  = title.getText().toString();
       // String work = amount.getText().toString();
        String date = dateView.getText().toString();
        String time = timeview.getText().toString();

        ExpenseOpenHelper openHelper = ExpenseOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.EXPENSE_TITLE,todotitle);
        contentValues.put(Contract.DATE,date);
        contentValues.put(Contract.TIME,time);


        long alarmtime =mainCalendar.getTimeInMillis();
        contentValues.put(Contract.ALARMTIME,alarmtime);
        long id = db.insert(Contract.EXPENSE_TABLE_NAME,null,contentValues);
//        Expense expense = new Expense(titleText,amount);
        Intent result = new Intent();
        result.putExtra(Contract.EXPENSE_ID,id);
//        result.putExtra(Constant.KEY_EXPENSE,expense);
        setResult(ADD_SUCCESS, result);
        finish();
    }

}

