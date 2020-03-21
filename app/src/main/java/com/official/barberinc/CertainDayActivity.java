package com.official.barberinc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CertainDayActivity extends AppCompatActivity implements VisitDialogFragment.DeleteDialogListener {

    public final static String TAG = "CertainDayActivity";

    private DatabaseHelper databaseHelper = new DatabaseHelper(this);;

    private Date date;
    private ArrayList <Visit> visits;
    private TextView dateView, dayNameView;
    private ViewGroup visitsLayout;

    private FloatingActionButton addVisitFab;

    private final int DP_HOUR_LENGTH = 60;
    private final int DP_DIVIDER_HEIGHT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
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

        Log.d(TAG, "onCreate finished");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");

        initVisits();
        setVisitsLayout();
    }


    private void setVisitsLayout() {
        Log.d(TAG, "setVisitsLayout called");
        visitsLayout = findViewById(R.id.visits_layout);
        visitsLayout.removeAllViews();

        for(Visit visit : visits) {
            VisitView visitView = new VisitView(this);
            visitView.setVisit(visit);

            // Params need to be type of parent layout, in this case - RelativeLayout
            RelativeLayout.LayoutParams visitViewParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.dpToPx(visit.getDurationMinutes(), this)
            );
            visitViewParams.setMargins(0,Utils.dpToPx(visit.getMinutesSinceStart(),this), 0, 0);

            visitView.setLayoutParams(visitViewParams);
            visitView.setBackgroundResource(getBackgroundDependentOnVisit(visit));
            visitView.requestLayout();
            visitView.setInnerViews();

            visitView.setOnLongClickListener(view -> {
                DialogFragment dialogFragment = VisitDialogFragment.newInstance(visit.getId(), visit.getName());
                dialogFragment.show(getSupportFragmentManager(), "dialog");
                return true;
            });
            visitsLayout.addView(visitView);
        }
        Log.d(TAG, "setVisitsLayout finished");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        int id = ((VisitDialogFragment) dialogFragment).id;
        String name = ((VisitDialogFragment) dialogFragment).name;

        if(databaseHelper.deleteVisitById(id))
            Toast.makeText(this, String.format("%s's visit deleted!", name), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Some error occurred!", Toast.LENGTH_SHORT).show();
        onResume();
    }

    @Override
    public void onDialogNeutralClick(DialogFragment dialogFragment) {
        Intent intent = new Intent(CertainDayActivity.this, NewVisitActivity.class);
        intent.putExtra(Utils.VISIT_ID_INTENT, Integer.toString(((VisitDialogFragment) dialogFragment).id));
        intent.putExtra(Utils.DATE_INTENT, new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        // Toast.makeText(this, "Cancel clicked!", Toast.LENGTH_SHORT).show();
        dialogFragment.dismiss();
    }

    private void setHoursLayout() {
        ViewGroup hoursLayout = findViewById(R.id.hours_layout);
        for(int i = 0; i < Utils.HOURS_LAYOUT_VALUES.length; i++) {
            TextView hourView = new TextView(this);
            hourView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dpToPx(DP_HOUR_LENGTH, this)));
            hourView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_tiny));
            hourView.setPadding(4,2,4,2);
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
        visits = databaseHelper.getDataFromCertainDay(new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));
    }

    private int getBackgroundDependentOnVisit(Visit visit) {
        if(visit.getTag() == Utils.VisitTypes.HAIRCUT)
            return R.drawable.visit_haircut_background;
        else if(visit.getTag() == Utils.VisitTypes.BARBER)
            return R.drawable.visit_barber_background;
        else
            return R.drawable.visit_combo_background;
    }


}
