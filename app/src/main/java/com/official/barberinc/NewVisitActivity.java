package com.official.barberinc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class NewVisitActivity extends AppCompatActivity {

    public static final String TAG = "NewVisitActivity";

    private static final class ActivityTypes {
        public static final int ADD = 1;
        public static final int EDIT = 2;
    }

    private DatabaseHelper databaseHelper;
    private Intent receivedIntent;

    private TextView dateView;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Date date;

    private TextView timeView;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private Date time;

    private TextView nameView;
    private RadioGroup radioGroup;
    private RadioButton checkedButton;
    private int tag = Utils.SpecialTags.BLANK;

    private Button applyButton;

    private int activityType;
    private int visitId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivedIntent = getIntent();
        databaseHelper = new DatabaseHelper(this);


        if(receivedIntent.hasExtra(Utils.DATE_INTENT)) {
            setContentView(R.layout.activity_new_visit_without_date);
            setDate();
            if(receivedIntent.hasExtra(Utils.VISIT_ID_INTENT))
                activityType = ActivityTypes.EDIT;
            else
                activityType = ActivityTypes.ADD;
        } else {
            setContentView(R.layout.activity_new_visit);
            setDateView();
            activityType = ActivityTypes.ADD;
        }

        setNameView();
        setTimeView();
        setRadioGroup();
        setApplyButton();


        if(activityType == ActivityTypes.EDIT) {
            setViewAccordingToVisit(Integer.parseInt(receivedIntent.getStringExtra(Utils.VISIT_ID_INTENT)));
        }
    }

    private void setViewAccordingToVisit(int id) {
        Visit v = databaseHelper.getVisitById(id);
        visitId = id;

        timeView.setText(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(v.getStart()));
        time = v.getStart();

        nameView.setText(v.getName());

        switch(v.getTag()) {
            case Utils.VisitTypes.HAIRCUT:
                radioGroup.check(R.id.radio_one);
                break;
            case Utils.VisitTypes.BARBER:
                radioGroup.check(R.id.radio_second);
                break;
            case Utils.VisitTypes.COMBO:
                radioGroup.check(R.id.radio_third);
                break;
        }


    }

    private void setNameView() {
        nameView = findViewById(R.id.name);
    }

    private void setDate() {
        try { date = new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).parse(receivedIntent.getStringExtra(Utils.DATE_INTENT)); }
        catch (ParseException e) { e.printStackTrace(); }
    }

    private void setDateView() {
        dateView = findViewById(R.id.date);
        dateView.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    NewVisitActivity.this,
                    R.style.DatePickerDialog,
                    onDateSetListener,
                    year, month, day);

            datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            datePickerDialog.show();
        });

        onDateSetListener = (view, year, month, dayOfMonth) -> {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            date = calendar.getTime();
            dateView.setText(new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));
        };
    }

    private void setTimeView() {
        timeView = findViewById(R.id.time);
        timeView.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int minute = calendar.get(Calendar.MINUTE);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    NewVisitActivity.this,
                    R.style.TimePickerDialog,
                    onTimeSetListener,
                    hour, minute, true);

            Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable((Color.WHITE))); // TODO: check it out
            timePickerDialog.show();
        });

        onTimeSetListener = (view, hourOfDay, minute) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            time = calendar.getTime();
            timeView.setText(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(time));
        };
    }

    private void setRadioGroup() {
        radioGroup = findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            checkedButton = NewVisitActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());

            switch (checkedButton.getText().toString()) {
                case "barber":
                    tag = Utils.VisitTypes.BARBER;
                    break;
                case "combo":
                    tag = Utils.VisitTypes.COMBO;
                    break;
                case "haircut":
                    tag = Utils.VisitTypes.HAIRCUT;
                    break;
                default:
                    tag = Utils.SpecialTags.WRONG;
            }
        });
    }

    private void setApplyButton() {
        applyButton = findViewById(R.id.apply_button);
        setApplyButtonText();

        applyButton.setOnClickListener(v -> {
            Log.d(TAG, "Apply button clicked!");
            if(validateName() && validateDate() && validateTime() && validateTag() && checkIntersections()) {

                String dateString = new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date);
                String timeString = new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(time);
                String datetimeString = dateString + " " + timeString;

                if (!databaseHelper.addData(new Visit(datetimeString, nameView.getText().toString(), tag))) {

                    Toast.makeText(NewVisitActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if(activityType == ActivityTypes.ADD)
                        Toast.makeText(NewVisitActivity.this, "New visit added!", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(NewVisitActivity.this, "Visit edited!", Toast.LENGTH_SHORT).show();
                        databaseHelper.deleteVisitById(visitId);
                    }

                    finish();
                }
            } else {
                Log.d(TAG, "Some error occurred!");
            }
        });
    }

    private void setApplyButtonText() {
        if(activityType == ActivityTypes.ADD)
            applyButton.setText("ADD");
        else
            applyButton.setText("EDIT");
    }

    private boolean validateTime() {
        if(time != null) {
            try {
                Calendar calendar = Calendar.getInstance();

                Calendar visitsStart = Calendar.getInstance();
                visitsStart.setTime(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).parse(Utils.WORKING_HOURS_START));
                visitsStart.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                Calendar visitsEnd = Calendar.getInstance();
                visitsEnd.setTime(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).parse(Utils.WORKING_HOURS_END));
                visitsEnd.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                if((time.before(visitsEnd.getTime()) && time.after(visitsStart.getTime())))
                    return true;
                else {
                    Toast.makeText(NewVisitActivity.this, "Visit needs to be between working hours!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (ParseException e) { e.printStackTrace(); }

        }
        Toast.makeText(NewVisitActivity.this, "Time was not provided!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean validateDate() {
        if(date != null)
            return true;
        else {
            Toast.makeText(NewVisitActivity.this, "Date was not provided!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validateName() {
        if(nameView.getText().length() > 0)
            return true;
        else {
            Toast.makeText(NewVisitActivity.this, "Name was not provided!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validateTag() {
        if (tag != Utils.SpecialTags.WRONG && tag != Utils.SpecialTags.BLANK)
            return true;
        else {
            Toast.makeText(NewVisitActivity.this, "Visit type was not selected!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkIntersections() {
        String datetimeString = new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date) + " " +
                new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(time);
        ArrayList <Visit> visits = databaseHelper.getDataFromCertainDay(new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));
        Visit todayVisit = new Visit(datetimeString, nameView.getText().toString(), tag);
        Log.d(TAG, String.format("Today's visits amount: %d", visits.size()));

        for(Visit v : visits) {
            if(v.getId() == visitId)
                continue;
            if((todayVisit.getStart().after(v.getStart()) && todayVisit.getStart().before(v.getEnd())) || (todayVisit.getStart().before(v.getStart()) && todayVisit.getEnd().after(v.getStart()))){
                Toast.makeText(NewVisitActivity.this, String.format("Visit intersects with %s's visit (%s-%s)", v.getName(),
                        new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(v.getStart()), new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(v.getEnd())),
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
}
