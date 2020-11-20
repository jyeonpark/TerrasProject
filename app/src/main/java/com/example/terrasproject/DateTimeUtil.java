package com.example.terrasproject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtil {
    public static String getToday(String pattern){
        DateFormat dtf = new SimpleDateFormat(pattern);
        final Calendar cal = Calendar.getInstance();
        return dtf.format(cal.getTime());
    }

    public static String getTomorrow(String pattern) {
        DateFormat dtf = new SimpleDateFormat(pattern);
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        return dtf.format(cal.getTime());
    }
}
