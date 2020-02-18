package com.official.barberinc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CertainDayActivity extends AppCompatActivity {

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CertainDayActivity.this, NewVisitActivity.class);
                intent.putExtra(Utils.DATE_INTENT, new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));
                startActivity(intent);
            }
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
    }
}
