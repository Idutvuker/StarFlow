package com.example.nick.starflow.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.nick.starflow.databases.types.StarData;

import java.util.List;

/**
 * Created by Nick on 27.06.2017.
 */

public class MyAdapter extends ArrayAdapter<StarData>
{
    private final LayoutInflater lInflater;
    private List<StarData> objects;

    private int resource;

    public MyAdapter(Context context, int resource, List<StarData> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.resource = resource;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        return view;
    }
}
