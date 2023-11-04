package com.example.kangai.Helpers;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    public static String toTitle(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    @SuppressLint("SimpleDateFormat")
    public static String timestampTo12HourFormat(Long timestamp){
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(date);
    }
}
