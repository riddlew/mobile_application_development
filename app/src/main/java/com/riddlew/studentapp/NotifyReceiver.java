package com.riddlew.studentapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotifyReceiver extends BroadcastReceiver {

    public static int NOTIFICATION_ID = 0;
    public static String CHANNEL_ID = "STUDENT_APP_NOTIFIER";

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra("text");
        createTimerNotificationChannel(context);
        createTimerNotification(context, text);
    }

    private void createTimerNotificationChannel(Context context) {
        if(Build.VERSION.SDK_INT < 26) return;

        CharSequence name = "NOTIFICATION_CHANNEL";
        String description = "Notification channel for student app.";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    private void createTimerNotification(Context context, String text) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Student App Notification")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(NOTIFICATION_ID, notification);
        NOTIFICATION_ID++;
    }
}