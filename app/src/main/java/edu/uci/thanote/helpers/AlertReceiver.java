package edu.uci.thanote.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import edu.uci.thanote.helpers.NotificationHelper;
import edu.uci.thanote.helpers.SharePreferencesHelper;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!allowNotify(context)) return;

        String title = "Daily notification from Thanote!";
        String message = getNotificationContent(context);
        NotificationHelper.getInstance(context).notify(title, message);
    }

    private boolean allowNotify(Context context) {
        String time = SharePreferencesHelper.getInstance(context).getTime();
        boolean isAlarmAllowed = SharePreferencesHelper.getInstance(context).isAlarmAllowed();
        return !time.isEmpty() && isAlarmAllowed;
    }

    private String getNotificationContent(Context context) {
        String title = SharePreferencesHelper.getInstance(context).getTitle();
        String message = SharePreferencesHelper.getInstance(context).getMessage();
        return title + "\n" + message;
    }
}
