package com.official.barberinc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Visit {

    private int id;
    private String datetime;
    private String date, time;
    private String name;
    private int tag;

    public Visit(String datetime, String name, int tag) {
        this.datetime = datetime;
        this.name = name;
        this.tag = tag;

        initDate();
    }

    public int getId() { return id; }

    public String getDatetime() { return datetime; }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public int getTag() {
        return tag;
    }

    private void initDate() {
        Date date;
        SimpleDateFormat datetimeFormat = new SimpleDateFormat(Utils.DateFormats.DATETIME_FORMAT);
        SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.DateFormats.DATE_FORMAT);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT);

        try {
            date = datetimeFormat.parse(datetime);
            this.date = dateFormat.format(date);
            this.time = timeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
