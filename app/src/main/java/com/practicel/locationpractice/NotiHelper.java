package com.practicel.locationpractice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

class NotiHelper extends ContextWrapper {

public static final String channel_id="com.example.androidlocationpractice";
public static final String channel_name="location2020";

NotificationManager manager;


    public NotiHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            createChannel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel channel = new NotificationChannel(channel_id,channel_name,NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);

    }

    public NotificationManager getManager() {

        if (manager==null)
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getRealtimeTrackingNoti(String title, String content, Uri defaultSound) {

        return new Notification.Builder(getApplicationContext(),channel_id)
                .setSmallIcon(R.drawable.ic_person_black_24dp)

                .setContentTitle(title).setContentText(content).setSound(defaultSound).setAutoCancel(false);
    }
}
