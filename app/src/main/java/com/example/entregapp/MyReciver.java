package com.example.entregapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.net.Inet4Address;

public class MyReciver extends BroadcastReceiver {

    public static String NOTIFICATIONID = "notification-id";
    public static String NOTIFICATION = "notification";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("gaboF", "entro al reciver");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(Configuracion.NOTIFICATION_CHANNEL_ID,"NOTIFICATION_CHANNEL_NAME",importance);
        assert notificationManager !=null;
        notificationManager.createNotificationChannel(notificationChannel);


        int id = intent.getIntExtra(NOTIFICATIONID, 0);
        assert notificationManager !=null;
        notificationManager.notify(id, notification);

    }
}
