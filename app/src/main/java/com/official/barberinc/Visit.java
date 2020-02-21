package com.official.barberinc;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Visit {

    private int id;
    private String datetime;
    private Date start, end;
    private String name;
    private int tag;

    private final int MILLIS_IN_MINUTE = 60000;

    private final static class StartTime {
        public static final int HOUR = 8;
        public static final int MINUTE = 0;
        public static final int SECOND = 0;
    }

    public Visit(String datetime, String name, int tag) {
        this.datetime = datetime;
        this.name = name;
        this.tag = tag;

        initDate();
    }

    public int getId() { return id; }

    public Date getStart() { return start; }

    public Date getEnd() { return end; }

    public String getDatetime() { return datetime; }

    public long getDurationMinutes() {
        return (end.getTime() - start.getTime()) / MILLIS_IN_MINUTE;
    }


    public long getMinutesSinceStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.HOUR_OF_DAY, StartTime.HOUR);
        calendar.set(Calendar.MINUTE, StartTime.MINUTE);
        calendar.set(Calendar.SECOND, StartTime.SECOND);
        return (start.getTime() - calendar.getTime().getTime()) / MILLIS_IN_MINUTE;
    }

    public String getName() {
        return name;
    }

    public int getTag() {
        return tag;
    }

    private void initDate() {
        int durationMinutes = 0;

        switch (tag) {
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

        try {
            start = new SimpleDateFormat(Utils.DateFormats.DATETIME_FORMAT).parse(datetime);
            end = new Date(start.getTime() + durationMinutes * MILLIS_IN_MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
