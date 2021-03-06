package com.practicel.locationpractice;

import android.app.Notification;
import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        remoteMessage.getData();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            sendNotiWithChannel(remoteMessage);
        }else {
            sendNoti(remoteMessage);
        }

        addRequestToUser(remoteMessage.getData());
    }

    private void addRequestToUser(Map<String, String> data) {

        DatabaseReference friend_request = FirebaseDatabase.getInstance().getReference(Common.USERS)
                .child(data.get(Common.TO_UID))
                .child(Common.FRIEND_REQUEST);
        User user = new User();
        user.setUid(data.get(Common.FROM_UID));
        user.setEmail(data.get(Common.FROM_NAME));

        friend_request.child(user.getUid()).setValue(user);




    }

    private void sendNoti(RemoteMessage remoteMessage) {
        Map<String,String> data = remoteMessage.getData();
        String title="request";
        String content = "new request from" + data.get(Common.FROM_NAME);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_person_black_24dp)
                .setContentTitle(title).setContentText(content).setSound(defaultSound).setAutoCancel(false);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(),builder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotiWithChannel(RemoteMessage remoteMessage) {

        Map<String,String> data = remoteMessage.getData();
        String title="request";
        String content = "new request from" + data.get(Common.FROM_NAME);
        NotiHelper helper;
        Notification.Builder builder;
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        helper = new NotiHelper(this);
        builder = helper.getRealtimeTrackingNoti(title,content,defaultSound);

        helper.getManager().notify(new Random().nextInt(),builder.build());

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){

            DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("tokens");
            tokens.child(user.getUid()).setValue(s);

        }

    }
}
