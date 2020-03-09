package edu.uci.thanote.scenes.notification;

import android.content.Context;
import edu.uci.thanote.R;
import edu.uci.thanote.apis.Api;
import edu.uci.thanote.helpers.SharePreferencesHelper;
import edu.uci.thanote.scenes.main.fragments.setting.ImageTextItem;

import java.text.DateFormat;
import java.util.Calendar;

public class NotificationViewModel {
    private Context context;
    private static final int DEFAULT_TIME_HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private static final int DEFAULT_TIME_MINUTE = Calendar.getInstance().get(Calendar.MINUTE);
    private static final ImageTextItem[] apis = new ImageTextItem[] {
        new ImageTextItem(R.drawable.ic_joke, Api.JOKE.toString()),
        new ImageTextItem(R.drawable.ic_recipe, Api.RECIPEPUPPY.toString()),
        new ImageTextItem(R.drawable.ic_movie, Api.THEMOVIEDB.toString()),
        new ImageTextItem(R.drawable.ic_cocktail, Api.THECOCKTAILDB.toString())
    };
    private int hour;
    private int minute;
    private String category;
    private boolean isAlarmAllowed;

    public NotificationViewModel(Context context) {
        this.context = context;
        this.hour = getHour();
        this.minute = getMinute();
        this.category = getCategory();
        this.isAlarmAllowed = getDefaultIsAlarmAllowed();
    }

    public int getHour() {
        // has time: "hh:mm"
        // no time: ""
        String time = SharePreferencesHelper.getInstance(context).getTime();
        if (time.isEmpty()) {
            return DEFAULT_TIME_HOUR;
        }
        int hour = Integer.parseInt(time.split(":")[0]);
        if (is24HourFormat()) {
            hour -= 12;
        }
        this.hour = hour;
        return hour;
    }

    public int getMinute() {
        // has time: "hh:mm"
        // no time: ""
        String time = SharePreferencesHelper.getInstance(context).getTime();
        if (time.isEmpty()) {
            return DEFAULT_TIME_MINUTE;
        }
        int minute =  Integer.parseInt(time.split(":")[1]);
        this.minute = minute;
        return minute;
    }

    public String getAlarmTime() {
        Calendar calendar = getCalendar(hour, minute);
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        return timeText;
    }

    public boolean is24HourFormat() {
        return android.text.format.DateFormat.is24HourFormat(context);
    }

    public void updateTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAlarmAllowed(boolean alarmAllowed) {
        isAlarmAllowed = alarmAllowed;
    }

    public int getDefaultCategorySpinnerPosition() {
        String category = getCategory();
        int index = 0;
        for (ImageTextItem api : apis) {
            if (api.getText().equals(category)) {
                return index;
            }

            index++;
        }
        return 0;
    }

    public boolean getDefaultIsAlarmAllowed() {
        return SharePreferencesHelper.getInstance(context).isAlarmAllowed();
    }

    public void save() {
        SharePreferencesHelper.getInstance(context).setTime(getTime());
        SharePreferencesHelper.getInstance(context).setCategory(category);
        SharePreferencesHelper.getInstance(context).setIsAlarmAllowed(isAlarmAllowed);
    }

    public Calendar getCalendar(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public ImageTextItem[] getApis() {
        return apis;
    }

    private String addLeadingZero(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    private String getTime() {
        String time = "";
        if (is24HourFormat()) {
            time += addLeadingZero(hour);
        } else {
            time += String.valueOf(hour);
        }

        time += ":" + addLeadingZero(minute);
        return time;
    }

    private String getCategory() {
        return SharePreferencesHelper.getInstance(context).getCategory();
    }
}
