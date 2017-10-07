package com.example.gaurav.todoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.jar.Manifest;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.example.gaurav.todoapp.Constant.KEY_EXPENSE;
import static com.example.gaurav.todoapp.Constant.KEY_EXPENSE_ID;
import static com.example.gaurav.todoapp.Constant.KEY_POSITION;
import static com.example.gaurav.todoapp.Constant.KEY_TITLE;

public class MainActivity extends AppCompatActivity  {

    public static final String MY_ACTION = "my_action";
    ListView listView;
    ArrayList<Expense> expenses;
    CustomAdapter adapter;
    BroadcastReceiver br;
    BroadcastReceiver broadcastReceiver;
    LocalBroadcastManager broadcastManager;
    Calendar calendar;
    AlarmManager alarmManager;

    public final static int REQUEST_DETAIL = 1;
    public final static int REQUEST_ADD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(MainActivity.this,"Custom Broadcast",Toast.LENGTH_SHORT).show();
            }
        };

        listView = (ListView)findViewById(R.id.listView);
        expenses = new ArrayList<>();


        ExpenseOpenHelper openHelper = ExpenseOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = openHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.EXPENSE_TABLE_NAME,null,null,null,null,null,null);
       // db.delete(Contract.EXPENSE_TABLE_NAME,Contract.EXPENSE_ID + " = ?",new String[]{"1"});

//        String[] columns = new String[]{Contract.EXPENSE_TITLE};
//        String[] selectionArgs = new String[]{"100","1000"};
//        db.query(Contract.EXPENSE_TABLE_NAME,columns,Contract.EXPENSE_AMOUNT + " > ? AND "
//                + Contract.EXPENSE_AMOUNT + " < ?",selectionArgs,null,null,
//                Contract.EXPENSE_AMOUNT + " DESC");

        while (cursor.moveToNext()){

            String title = cursor.getString(cursor.getColumnIndex(Contract.EXPENSE_TITLE));
            String date = cursor.getString(cursor.getColumnIndex(Contract.DATE));
            String time = cursor.getString(cursor.getColumnIndex(Contract.TIME));
            int id = cursor.getInt(cursor.getColumnIndex(Contract.EXPENSE_ID));
            Expense expense = new Expense(title,date,time,id);
            expenses.add(expense);

        }

        cursor.close();


        adapter = new CustomAdapter(this, expenses, new CustomAdapter.DeleteButtonClickListener() {
            @Override
            public void onDeleteClicked(int position,View view) {
                expenses.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Expense expense = expenses.get(i);
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);

                intent.putExtra(KEY_EXPENSE,expense);
                intent.putExtra(KEY_POSITION,i);

                startActivityForResult(intent,REQUEST_DETAIL);
            }
        });

    }
//    public void sendBroadcast(View view){
//
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        Intent alarmIntent = new Intent(this,AlarmReceiver.class);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,alarmIntent,0);
//
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5000,60000,pendingIntent);


//        Intent intent = new Intent(this,AlarmReceiver.class);
//        intent.setAction(MY_ACTION);
//        sendBroadcast(intent);
//        broadcastManager.sendBroadcast(intent);

  //  }

    @Override
    protected void onStart() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(MY_ACTION);
        broadcastManager.registerReceiver(broadcastReceiver,intentFilter);
//        br = new CustomBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
//        registerReceiver(br,intentFilter);
//
        super.onStart();
//        Log.d("Lifecycle Event","Start");
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(MY_ACTION);
        broadcastManager.registerReceiver(broadcastReceiver,intentFilter);
    }
    @Override
    protected void onPause() {

        broadcastManager.unregisterReceiver(broadcastReceiver);
        super.onPause();


    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(br);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {

            Intent add = new Intent(this,AddExpenseActivity.class);
            startActivityForResult(add,REQUEST_ADD);



        }else if(id == R.id.remove){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Expense");
            builder.setMessage("Are you sure?");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    expenses.remove(expenses.size() -1);
                    adapter.notifyDataSetChanged();
                    ExpenseOpenHelper openHelper = ExpenseOpenHelper.getInstance(getApplicationContext());
                    SQLiteDatabase db = openHelper.getReadableDatabase();
                    db.delete(Contract.EXPENSE_TABLE_NAME,Contract.EXPENSE_ID + " = ?",new String[]{"1"});

                }
            });
            builder.setNegativeButton("No", null);

            AlertDialog dialog = builder.create();
            dialog.show();

        }

        if( id == R.id.about){

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String url = "http://codingninjas.in";
            intent.setData(Uri.parse(url));
            startActivity(intent);

        }else if(id == R.id.feedback){

            Intent feedback = new Intent();
            feedback.setAction(Intent.ACTION_SENDTO);
            feedback.setData(Uri.parse("mailto:jyotijakhar99@gmail.com"));
            feedback.putExtra(Intent.EXTRA_SUBJECT,"FEEDBACK");
            startActivity(feedback);
            if(feedback.resolveActivity(getPackageManager()) != null){
                startActivity(feedback);
            }

        }else  if(id == R.id.share){

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT,"DOWNLOAD our app");
            Intent chooser = Intent.createChooser(share,"Share App");
            startActivity(chooser);

        }
        else if(id == R.id.call){

            int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
            if(permission == PERMISSION_GRANTED){
                Intent call = new Intent();
                call.setAction(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:8860333723"));
                startActivity(call);
            }
            else {
                //  ActivityCompat.requestPermissions();
            }


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_DETAIL){

            if(resultCode == DetailActivity.RESULT_SAVE){


                int position = data.getIntExtra(KEY_POSITION,0);
                String title = data.getStringExtra(KEY_TITLE);

                Expense expense = expenses.get(position);
                expense.setTitle(title);
                adapter.notifyDataSetChanged();
            }
        }else if(requestCode == REQUEST_ADD){
            if(resultCode == AddExpenseActivity.ADD_SUCCESS){
                long id   =  data.getLongExtra(Contract.EXPENSE_ID,-1L);
                if(id > -1){
                    ExpenseOpenHelper openHelper = ExpenseOpenHelper.getInstance(getApplicationContext());
                    SQLiteDatabase db = openHelper.getReadableDatabase();

                    Cursor cursor = db.query(Contract.EXPENSE_TABLE_NAME,null,
                            Contract.EXPENSE_ID + " = ?",new String[]{id + ""}
                            ,null,null,null);

                    if(cursor.moveToFirst()){
                        String todo = cursor.getString(cursor.getColumnIndex(Contract.EXPENSE_TITLE));
                        String date = cursor.getString(cursor.getColumnIndex(Contract.DATE));
                        String time = cursor.getString(cursor.getColumnIndex(Contract.TIME));
                        long alarmtime = cursor.getInt(cursor.getColumnIndex(Contract.ALARMTIME));
                        int expense_id = cursor.getInt((cursor.getColumnIndex(Contract.EXPENSE_ID)));
                        Expense expense = new Expense(todo,date,time,(int)id);
                        expenses.add(expense);
                        adapter.notifyDataSetChanged();
                        setAlarm(alarmtime,todo,expense_id);
                    }

                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void setAlarm(long alarmtime,String title,int id)
    {

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarmIntent = new Intent(this,AlarmReceiver.class);
        alarmIntent.putExtra("todo",title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,id,alarmIntent,0);
//        alarmManager.cancel();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmtime,60000,pendingIntent);
    }
}

