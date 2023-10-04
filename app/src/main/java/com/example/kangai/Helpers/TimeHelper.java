package com.example.kangai.Helpers;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeHelper {
    public static String millisToReadable(Long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        Date date = new Date(timestamp);
        return sdf.format(date);
    }
}
