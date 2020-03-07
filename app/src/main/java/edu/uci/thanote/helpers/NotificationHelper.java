package edu.uci.thanote.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.RingtoneManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import edu.uci.thanote.R;

public class NotificationHelper extends ContextWrapper {
    private static final int NOTIFICATION_ID = 1116;
    private static final String NOTIFICATION_CHANNEL_ID = "edu.uci.thanote.notification_channel_id";
    private static final String NOTIFICATION_CHANNEL_NAME = "Thanote Channel";

    private static NotificationHelper instance;
    private NotificationManager manager;

    private NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public static NotificationHelper getInstance(Context base) {
        if (instance == null) {
            instance = new NotificationHelper(base);
        }
        return instance;
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return manager;
    }

    private NotificationCompat.Builder getChannelNotification(String title, String message) {
        return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true);
    }

    public void notify(String title, String message) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification notification = getChannelNotification(title, message).build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
