package edu.uci.thanote.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesHelper {
    private static SharePreferencesHelper sharePreferencesHelper = new SharePreferencesHelper();
    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor editor = null;
    private static final String SHARE_PREFERENCE_NAME = "ThanoteSharePreferences";
    private static final String NOTIFICATION_TIME = "notification_time";
    private static final String NOTIFICATION_CATEGORY = "notification_category";
    private static final String NOTIFICATION_TITLE = "notification_title";
    private static final String NOTIFICATION_MESSAGE = "notification_message";
    private static final String NOTIFICATION_IS_ALARM_ALLOWED = "notification_is_alarm_allowed";

    private SharePreferencesHelper() {}

    public static SharePreferencesHelper getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.apply();
        }

        return sharePreferencesHelper;
    }

    public String getTime() {
        return getString(NOTIFICATION_TIME);
    }

    public String getCategory() {
        return getString(NOTIFICATION_CATEGORY);
    }

    public String getTitle() {
        return getString(NOTIFICATION_TITLE);
    }

    public String getMessage() {
        return getString(NOTIFICATION_MESSAGE);
    }

    public boolean isAlarmAllowed() {
        return sharedPreferences.getBoolean(NOTIFICATION_IS_ALARM_ALLOWED, true);
    }

    public void setTime(String time) {
        setString(NOTIFICATION_TIME, time);
    }

    public void setCategory(String category) {
        setString(NOTIFICATION_CATEGORY, category);
    }

    public void setTitle(String title) {
        setString(NOTIFICATION_TITLE, title);
    }

    public void setMessage(String message) {
        setString(NOTIFICATION_MESSAGE, message);
    }

    public void setIsAlarmAllowed(boolean isAllowed) {
        editor.putBoolean(NOTIFICATION_IS_ALARM_ALLOWED, isAllowed);
        editor.commit();
    }

    public void reset() {
        editor.clear();
        editor.commit();
    }

    private String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    private void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
}
