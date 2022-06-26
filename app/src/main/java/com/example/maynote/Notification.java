package com.example.maynote;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class Notification extends BroadcastReceiver {

    public static final int notificationID = 1;
    public static final String channelID = "Maynote";
    public static final String titleExtra = "titleExtra";
    public static final String messageExtra = "messageExtra";

    @Override
    public void onReceive(Context context, Intent intent) {

        android.app.Notification notification = new NotificationCompat.Builder(context,channelID)
                .setSmallIcon(R.drawable.icon_lembretes)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra))
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID,notification);
    }
}
