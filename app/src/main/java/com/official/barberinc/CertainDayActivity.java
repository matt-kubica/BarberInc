package com.official.barberinc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CertainDayActivity extends AppCompatActivity {

    private Date date;
    private ArrayList <Visit> visits;
    private TextView dateView, dayNameView;

    private FloatingActionButton addVisitFab;

    private final int DP_HOUR_LENGTH = 60;
    private final int DP_DIVIDER_HEIGHT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certain_day);

        initDate();
        initVisits();

        dateView = findViewById(R.id.date_view);
        dateView.setText(new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));

        dayNameView = findViewById(R.id.day_name_view);
        dayNameView.setText(Utils.DAY_NAMES[getCurrentDayOfWeek() - 1]);

        addVisitFab = findViewById(R.id.fab);
        addVisitFab.setOnClickListener(v -> {
            Intent intent = new Intent(CertainDayActivity.this, NewVisitActivity.class);
            intent.putExtra(Utils.DATE_INTENT, new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));
            startActivity(intent);
        });

        setZebraLayout();
        setHoursLayout();
        setVisitsLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        initVisits();
        setVisitsLayout();
    }

    private void setVisitsLayout() {
        ViewGroup visitsLayout = findViewById(R.id.visits_layout);
        for(Visit v : visits) {
            RelativeLayout innerLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams innerLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(v.getDurationMinutes(), this));
            innerLayoutParams.setMargins(0,Utils.dpToPx(v.getMinutesSinceStart(),this), 0, 0);
            innerLayout.setLayoutParams(innerLayoutParams);

            TextView visitView = new TextView(this);
            RelativeLayout.LayoutParams visitViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            visitViewParams.setMargins(4,4,4,4);
            visitView.setLayoutParams(visitViewParams);
            visitView.setBackgroundResource(setBackgroundDependentOnVisit(v));
            visitView.setPadding(8,4,8,4);

            // TODO: Very poor, change it later, best option would be to write own view
            String text = new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(v.getStart()) + " - " +new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(v.getEnd()) + "        " + v.getName();
            visitView.setText(text);


            innerLayout.addView(visitView);
            visitsLayout.addView(innerLayout);
        }
    }

    private void setHoursLayout() {
        ViewGroup hoursLayout = findViewById(R.id.hours_layout);
        for(int i = 0; i < Utils.HOURS_LAYOUT_VALUES.length; i++) {
            TextView hourView = new TextView(this);
            hourView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dpToPx(DP_HOUR_LENGTH, this)));
            hourView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_tiny));
            hourView.setPadding(4,4,4,4);
            hourView.setText(Utils.HOURS_LAYOUT_VALUES[i]);
            hoursLayout.addView(hourView);
        }
    }

    private void setZebraLayout() {
        ViewGroup zebraLayout = findViewById(R.id.zebra_layout);
        for(int i = 0; i < Utils.HOURS_LAYOUT_VALUES.length ; i++) {
            View dividerView = new View(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(DP_DIVIDER_HEIGHT, this));
            params.setMargins(16, Utils.dpToPx(i * DP_HOUR_LENGTH , this), 16,0);
            dividerView.setLayoutParams(params);
            dividerView.setBackgroundColor(getResources().getColor(R.color.colorDivider));
            zebraLayout.addView(dividerView);
        }
    }

    private int getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
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
    }

    private int setBackgroundDependentOnVisit(Visit visit) {
        if(visit.getTag() == Utils.VisitTypes.HAIRCUT)
            return R.drawable.visit_haircut_background;
        else if(visit.getTag() == Utils.VisitTypes.BARBER)
            return R.drawable.visit_barber_background;
        else
            return R.drawable.visit_combo_background;
    }
}
