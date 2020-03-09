package edu.uci.thanote.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import edu.uci.thanote.scenes.notification.AlertReceiver;

import java.text.DateFormat;
import java.util.Calendar;

public class AlarmService extends Service {
    private static final int ALARM_REQUEST_CODE = 123;
    private static final int DEFAULT_TIME_HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private static final int DEFAULT_TIME_MINUTE = Calendar.getInstance().get(Calendar.MINUTE);
    private static final String REGEX = ":";

    public AlarmService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        checkAlarm();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    private void checkAlarm() {
        if (getDefaultIsAlarmAllowed()) {
            startAlarm();
        } else {
            cancelAlarm();
        }
    }

    private void startAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, getHour());
        calendar.set(Calendar.MINUTE, getMinute());
        calendar.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, intent, 0);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private int getHour() {
        // has time: "hh:mm"
        // no time: ""
        String time = SharePreferencesHelper.getInstance(getApplicationContext()).getTime();
        if (time.isEmpty()) {
            return DEFAULT_TIME_HOUR;
        }
        int hour = Integer.parseInt(time.split(REGEX)[0]);
        if (is24HourFormat()) {
            hour -= 12;
        }
        return hour;
    }

    private int getMinute() {
        // has time: "hh:mm"
        // no time: ""
        String time = SharePreferencesHelper.getInstance(getApplicationContext()).getTime();
        if (time.isEmpty()) {
            return DEFAULT_TIME_MINUTE;
        }
        int minute =  Integer.parseInt(time.split(REGEX)[1]);
        return minute;
    }

    private boolean is24HourFormat() {
        return android.text.format.DateFormat.is24HourFormat(getApplicationContext());
    }

    private boolean getDefaultIsAlarmAllowed() {
        return SharePreferencesHelper.getInstance(getApplicationContext()).isAlarmAllowed();
    }
}
