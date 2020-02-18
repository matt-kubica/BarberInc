package com.official.barberinc;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList <Visit> listItems;
    private LayoutInflater layoutInflater;

    private TextView startTimeView, endTimeView;
    private TextView nameView;

    public ListViewAdapter(Context context, ArrayList <Visit> listItems) {
        this.context = context;
        this.listItems = listItems;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listItems.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.activity_list_view, null);
        startTimeView = view.findViewById(R.id.start_time);
        endTimeView = view.findViewById(R.id.end_time);
        nameView = view.findViewById(R.id.name);
        setView(i);

        return view;
    }

    private void setView(int i) {
        int durationMinutes = 0;
        final int milisInMinute = 60000;

        switch (listItems.get(i).getTag()) {
            case Utils.VisitTypes.HAIRCUT:
                durationMinutes = Utils.Durations.HAIRCUT_DURATION_MINUTES;
                break;
            case Utils.VisitTypes.BARBER:
                durationMinutes = Utils.Durations.BARBER_DURATION_MINUTES;
                break;
            case Utils.VisitTypes.COMBO:
                durationMinutes = Utils.Durations.COMBO_DURATION_MINUTES;
                break;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT);
        Date time;
        try {
            time = dateFormat.parse(listItems.get(i).getTime());
            String startTimeString = dateFormat.format(time);
            String endTimeString = dateFormat.format(new Date(time.getTime() + durationMinutes * milisInMinute));
            startTimeView.setText(startTimeString);
            endTimeView.setText(endTimeString);

        } catch (ParseException e) {
            e.printStackTrace();
        }



        nameView.setText(listItems.get(i).getName());

    }


}

