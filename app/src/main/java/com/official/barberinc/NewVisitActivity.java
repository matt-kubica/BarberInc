package com.official.barberinc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private int tag;

    private Button applyButton;

    private int activityType;

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
            Log.d(TAG, "ID = " + receivedIntent.getStringExtra(Utils.VISIT_ID_INTENT));
            setViewAccordingToVisit(Integer.parseInt(receivedIntent.getStringExtra(Utils.VISIT_ID_INTENT)));
        }
    }

    private void setViewAccordingToVisit(int id) {
        Visit v = databaseHelper.getVisitById(id);

        timeView.setText(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(v.getStart()));
        time = v.getStart();

        nameView.setText(v.getName());

//        switch(v.getTag()) {
//            case Utils.VisitTypes.HAIRCUT:
//                radioGroup.check(R.id.radio_haircut);
//                break;
//            case Utils.VisitTypes.BARBER:
//                radioGroup.check(R.id.radio_barber);
//                break;
//            case Utils.VisitTypes.COMBO:
//                radioGroup.check(R.id.radio_combo);
//                break;
//        }

        databaseHelper.deleteVisitById(id);
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
            int hour = calendar.get(Calendar.HOUR);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    NewVisitActivity.this,
                    R.style.TimePickerDialog,
                    onTimeSetListener,
                    hour, minute, true);

            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.WHITE)));
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
            checkedButton = findViewById(checkedId);

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
                    tag = Utils.specialTags.WRONG;
            }
        });
    }

    private void setApplyButton() {
        applyButton = findViewById(R.id.apply_button);
        if(activityType == ActivityTypes.ADD)
            applyButton.setText("ADD");
        else
            applyButton.setText("EDIT");

        applyButton.setOnClickListener(v -> {
            if(nameView.getText().length() > 0 &&
             date != null && time != null && tag != Utils.specialTags.WRONG) {
                String dateString = new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date);
                String timeString = new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(time);
                String datetimeString = dateString + " " + timeString;

                if (!databaseHelper.addData(new Visit(datetimeString, nameView.getText().toString(), tag))) {
                    Toast.makeText(NewVisitActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if(activityType == ActivityTypes.ADD)
                        Toast.makeText(NewVisitActivity.this, "New visit added!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(NewVisitActivity.this, "Visit edited!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
//                if(!validTime())
//                    Toast.makeText(NewVisitActivity.this, "Visit needs to be between working hours!", Toast.LENGTH_SHORT).show();
//                else
                Toast.makeText(NewVisitActivity.this, "One of fields were not provided!", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private boolean validTime() {
//        if(time != null) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(time);
//            calendar.add(Calendar.DATE, 1);
//            Date visitTime = calendar.getTime();
//
//
//
//
//            try {
//                Calendar calendarStart = Calendar.getInstance();
//                calendarStart.setTime(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).parse(Utils.WORKING_HOURS_START));
//                calendarStart.add(Calendar.DATE, 1);
//
//                Calendar calendarEnd = Calendar.getInstance();
//                calendarEnd.setTime(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).parse(Utils.WORKING_HOURS_END));
//                calendarEnd.add(Calendar.DATE, 1);
//
//                return (visitTime.before(calendarEnd.getTime()) &&
//                        visitTime.after(calendarStart.getTime()));
//            } catch (ParseException e) { e.printStackTrace(); }
//        }
//        return false;
//    }
//
//    private boolean validDate() {
//        return date != null;
//    }
}
