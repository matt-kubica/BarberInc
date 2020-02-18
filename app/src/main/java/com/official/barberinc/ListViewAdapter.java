package com.official.barberinc;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList <Visit> visits;
    private LayoutInflater layoutInflater;

    private TextView startTimeView, endTimeView;
    private TextView nameView;

    public ListViewAdapter(Context context, ArrayList <Visit> visits) {
        this.context = context;
        this.visits = visits;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return visits.size();
    }

    @Override
    public Object getItem(int i) {
        return visits.get(i);
    }

    @Override
    public long getItemId(int i) {
        return visits.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.activity_list_view, null);

        startTimeView = view.findViewById(R.id.start_time);
        endTimeView = view.findViewById(R.id.end_time);
        nameView = view.findViewById(R.id.name);

        startTimeView.setText(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(visits.get(i).getStartTime()));
        endTimeView.setText(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(visits.get(i).getEndTime()));
        nameView.setText(visits.get(i).getName());

        return view;
    }
}

