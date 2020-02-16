package edu.uci.thanote.databases.general;

import android.annotation.SuppressLint;
import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampConverter {
    // other format: http://tutorials.jenkov.com/java-date-time/parsing-formatting-dates.html
    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @SuppressLint("SimpleDateFormat")
    public static DateFormat dateFormat = new SimpleDateFormat(TIME_STAMP_FORMAT);

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value == null) return null;

        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
