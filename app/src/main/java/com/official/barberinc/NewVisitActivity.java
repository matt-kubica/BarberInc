package com.official.barberinc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewVisitActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent receivedIntent = getIntent();
        databaseHelper = new DatabaseHelper(this);

        if(receivedIntent.getStringExtra(Utils.DATE_INTENT).length() == 0) {
            setContentView(R.layout.activity_new_visit);
            setDateView();
        } else {
            setContentView(R.layout.activity_new_visit_without_date);

            try {
                date = new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).parse(receivedIntent.getStringExtra(Utils.DATE_INTENT));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        nameView = findViewById(R.id.name);
        setTimeView();
        setRadioGroup();
        setApplyButton();
    }

    private void setDateView() {
        dateView = findViewById(R.id.date);
        // dateView.setText(new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(Calendar.getInstance().getTime()));
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date = calendar.getTime();
                dateView.setText(new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date));
            }
        };
    }

    private void setTimeView() {
        timeView = findViewById(R.id.time);
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                time = calendar.getTime();
                timeView.setText(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(time));
            }
        };
    }

    private void setRadioGroup() {
        radioGroup = findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
                        tag = Utils.SpecialTags.WRONG;
                }
            }
        });
    }

    private void setApplyButton() {
        applyButton = findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameView.getText().length() > 0 &&
                date != null && time != null && tag != Utils.SpecialTags.WRONG) {
                    String dateString = new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT).format(date);
                    String timeString = new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(time);
                    String datetimeString = dateString + " " + timeString;

                    if (!databaseHelper.addData(new Visit(datetimeString, nameView.getText().toString(), tag))) {
                        Toast.makeText(NewVisitActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NewVisitActivity.this, "New visit added!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(NewVisitActivity.this, "One of fields were not provided!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
