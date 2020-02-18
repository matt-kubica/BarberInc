package com.official.barberinc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CertainDayActivity extends AppCompatActivity {

    public static final String TAG = "CertainDayActivity";

    private Date date;
    private ArrayList <Visit> visits;
    private TextView dateView;
    private ListView listView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certain_day);

        initDate();
        initVisits();

        dateView = findViewById(R.id.date);
        dateView.setText(new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));

        listView = findViewById(R.id.list_view);
        listView.setAdapter(new ListViewAdapter(getApplicationContext(), visits));

        fab = findViewById(R.id.add_new_visit_fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CertainDayActivity.this, NewVisitActivity.class);
            intent.putExtra(Utils.DATE_INTENT, new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));
            startActivity(intent);
        });
    }

    private void initDate() {
        Intent receivedIntent = getIntent();
        String dateString = receivedIntent.getStringExtra(Utils.DATE_INTENT);
        SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT);
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initVisits() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        visits = databaseHelper.getDataFromCertainDay(new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));
        // TODO: put blank fields in list
        calculateGaps();

    }

    private void calculateGaps() {
        if(visits.size() != 0) {
            Log.d(TAG, "calculateGaps: started");

            // adding gaps between visits
            int size = visits.size();
            for(int i = 1; i < size; i++) {
                visits.add(new Visit(visits.get(i - 1).getEndTime(), visits.get(i).getStartTime(), "blank", Utils.SpecialTags.BLANK));
            }

            // beginning gap
            visits.add(new Visit(new Date(date.getTime() + 8 * Utils.HOUR), visits.get(0).getStartTime(), "blank", Utils.SpecialTags.BLANK));

            // ending gap
            visits.add(new Visit(visits.get(visits.size() - 1).getEndTime(), new Date(date.getTime() + 23 * Utils.HOUR), "blank", Utils.SpecialTags.BLANK));

            // ordering
            visits.stream().sorted((o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));
            Log.d(TAG, "calculateGaps: finished");
        }



    }
}
