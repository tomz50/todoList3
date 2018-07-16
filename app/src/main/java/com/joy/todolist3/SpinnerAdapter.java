package com.joy.todolist3;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private ArrayList<ColorItem> color_lists;

    public SpinnerAdapter(Context c, ArrayList<ColorItem> color_lists) {
        inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        this.color_lists = color_lists;
    }

    @Override
    public int getCount() {
        return color_lists.size();
    }

    @Override
    public Object getItem(int position) {
        return color_lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return color_lists.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColorItem colorItem = (ColorItem) getItem(position);
        View v = inflater.inflate(R.layout.color_view,null);
        ImageView ticket = v.findViewById(R.id.ticket);
        TextView color_name = v.findViewById(R.id.color_name);
        ticket.setBackgroundColor(Color.parseColor(colorItem.code));
        color_name.setText(colorItem.name);
        return v;
    }
}
