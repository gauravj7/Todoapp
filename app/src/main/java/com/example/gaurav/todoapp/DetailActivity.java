package com.example.gaurav.todoapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.gaurav.todoapp.Constant.KEY_EXPENSE;
import static com.example.gaurav.todoapp.Constant.KEY_POSITION;
import static com.example.gaurav.todoapp.Constant.KEY_TITLE;

public class DetailActivity extends AppCompatActivity {

    EditText title;
    int position;

    public static final int RESULT_SAVE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        if(i.getAction() == Intent.ACTION_VIEW){
            Uri uri  = i.getData();
            String url = uri.getScheme() + uri.getHost();
        }
//

        title = (EditText)findViewById(R.id.titleEdit);


        if(i.hasExtra(KEY_EXPENSE)){
            Expense expense =(Expense) i.getSerializableExtra(KEY_EXPENSE);
            position = i.getIntExtra(KEY_POSITION,0);
            title.setText(expense.getTitle());
        }



    }

    public void buttonClicked(View view){

        String updatedTitle = title.getEditableText().toString();

        Intent i = new Intent();
        i.putExtra(KEY_TITLE,updatedTitle);
        i.putExtra(KEY_POSITION,position);
        setResult(RESULT_SAVE,i);
        finish();


    }

}
