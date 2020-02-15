package edu.uci.thanote.databases.general;

import android.annotation.SuppressLint;
import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConverter {
    // other format: http://tutorials.jenkov.com/java-date-time/parsing-formatting-dates.html
    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @SuppressLint("SimpleDateFormat")
    public static DateFormat dateFormat = new SimpleDateFormat(TIME_STAMP_FORMAT);

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }
}
