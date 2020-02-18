package com.official.barberinc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Visit {

    private int id;
    private String datetime;
    private Date start, end;
    private String name;
    private int tag;

    public Visit(String datetime, String name, int tag) {
        this.datetime = datetime;
        this.name = name;
        this.tag = tag;

        initDate();
    }

    public Visit(Date start, Date end, String name, int tag) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.tag = tag;

        initDatetime();
    }

    public int getId() { return id; }

    public String getDatetime() { return datetime; }

    public Date getStartTime() {
        return start;
    }

    public Date getEndTime() { return end; }

    public String getName() {
        return name;
    }

    public int getTag() {
        return tag;
    }

    private void initDate() {
        int durationMinutes = 0;
        final int milisInMinute = 60000;

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
            end = new Date(start.getTime() + durationMinutes * milisInMinute);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initDatetime() {
        datetime = new SimpleDateFormat(Utils.DateFormats.DATETIME_FORMAT).format(start);
    }

}
