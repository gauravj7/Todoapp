package com.example.gaurav.todoapp;

/**
 * Created by GAURAV on 9/27/2017.
 */

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ralph on 09/09/17.
 */

public class CustomAdapter extends ArrayAdapter<Expense> {


    Context mContext;
    ArrayList<Expense> mItems;
    DeleteButtonClickListener mDeleteButtonClickListener;
    CheckBox cb;

    public CustomAdapter(@NonNull Context context, ArrayList<Expense> expenses,CheckBox checkBox,DeleteButtonClickListener deleteButtonClickListener) {
        super(context, 0);

        mContext = context;
        mItems = expenses;
        mDeleteButtonClickListener = deleteButtonClickListener;
        cb = checkBox;
        

    }
    public CustomAdapter(@NonNull Context context, ArrayList<Expense> expenses,DeleteButtonClickListener deleteButtonClickListener) {
        super(context, 0);

        mContext = context;
        mItems = expenses;
        mDeleteButtonClickListener = deleteButtonClickListener;


    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_row_layout,null);
            viewHolder = new ViewHolder();
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            Button button = (Button)convertView.findViewById(R.id.deleteButton);
            CheckBox cb = (CheckBox)convertView.findViewById(R.id.check);
            viewHolder.cb = cb;
            viewHolder.button = button;
            viewHolder.title = title;
            viewHolder.date = date;
            viewHolder.time = time;
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder)convertView.getTag();
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                expenses.remove(position);
//                adapter.notifyDataSetChanged();
                mDeleteButtonClickListener.onDeleteClicked(position,view);
            }
        });
        final View finalConvertView = convertView;
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = (TextView) finalConvertView.findViewById(R.id.title);
                if(!finalViewHolder.cb.isChecked()) {

                    title.setPaintFlags(title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                else
                {
                    title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });
        Expense item = mItems.get(position);
        viewHolder.title.setText(item.getTitle());
        viewHolder.date.setText(item.getdate());
        viewHolder.time.setText(item.gettime());
        return convertView;
    }


    static class ViewHolder {

        TextView title;
        TextView date;
        TextView time;
        Button button;
        CheckBox cb;

    }


    static interface DeleteButtonClickListener {

        void onDeleteClicked(int position,View v);

    }
}
