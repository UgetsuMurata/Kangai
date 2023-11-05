package com.example.kangai.Helpers;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeHelper {
    public static String millisToReadable(Long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        Date date = new Date(timestamp);
        return sdf.format(date);
    }
    public static Long readableToMillis(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        Date date = sdf.parse(dateStr);
        if (date != null) {
            return date.getTime();
        }
        return null; // Return null if parsing fails
    }
}
