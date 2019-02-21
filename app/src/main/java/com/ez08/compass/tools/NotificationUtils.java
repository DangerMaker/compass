package com.ez08.compass.tools;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.ez08.compass.R;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    public NotificationUtils(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content) {
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.logo_)
                .setAutoCancel(true);
        Intent talkIntent = new Intent();
        talkIntent.setAction("force_logout");
        talkIntent.putExtra("force_message",content);
        talkIntent.setClassName("com.ez08.compass",
                "com.ez08.compass.ui.MainActivity");
        PendingIntent pi = PendingIntent.getActivity(
                getBaseContext(), 0, talkIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        return builder;
    }

    public NotificationCompat.Builder getNotification_25(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.logo_)
                .setAutoCancel(true);
        Intent talkIntent = new Intent();
        talkIntent.setAction("force_logout");
        talkIntent.putExtra("force_message",content);
        talkIntent.setClassName("com.ez08.compass",
                "com.ez08.compass.ui.MainActivity");
        PendingIntent pi = PendingIntent.getActivity(getBaseContext(), 0, talkIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        return builder;
    }

    public void sendNotification(String title, String content) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content).build();
            getManager().notify(1, notification);
        } else {
            Notification notification = getNotification_25(title, content).build();
            getManager().notify(1, notification);
        }
    }
}