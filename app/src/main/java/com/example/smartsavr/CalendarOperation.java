package com.example.smartsavr;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarOperation {
    Calendar cal;
    int sec;
    int min;
    int hour;
    int dayWeek;
    int dayMon;

    public CalendarOperation() {
        cal = Calendar.getInstance();
        // TODO: support other time zones
        cal.setTimeZone(TimeZone.getTimeZone("EST"));
        sec = cal.get(Calendar.SECOND) * 1000;
        min = cal.get(Calendar.MINUTE) * 60 * 1000;
        hour = cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000;
        dayWeek = cal.get(Calendar.DAY_OF_WEEK) * 24 * 60 * 60 * 1000;
        dayMon = cal.get(Calendar.DAY_OF_MONTH) * 24 * 60 * 60 * 1000;
    }

    public long calMillisWeek() {
        return System.currentTimeMillis() - (dayWeek + hour + min + sec);

    }

    public long calMillisMonth() {
        return System.currentTimeMillis() - (dayMon + hour + min + sec);
    }
}
