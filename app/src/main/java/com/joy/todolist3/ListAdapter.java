package com.joy.todolist3;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListAdapter extends CursorAdapter {

    LayoutInflater inflater;

    public ListAdapter(Context context, Cursor list) {
        super(context, list, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return inflater.inflate(R.layout.item_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        LinearLayout container = view.findViewById(R.id.bg_memo);
        TextView date = view.findViewById(R.id.txtAlarm);
        TextView memo = view.findViewById(R.id.txtName);
        Log.i("color=", cursor.getString(4));
        date.setText(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        memo.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        container.setBackgroundColor(Color.parseColor("#"+cursor.getString(4)));

    }
}
