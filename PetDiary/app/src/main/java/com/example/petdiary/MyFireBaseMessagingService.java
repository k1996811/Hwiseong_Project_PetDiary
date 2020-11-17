package com.example.petdiary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.petdiary.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService{

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("FCM_PetDiary", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageBody = remoteMessage.getNotification().getBody();
        String messageTitle = remoteMessage.getNotification().getTitle();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Channel ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Channel Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());

//        String title = remoteMessage.getNotification().getTitle();//firebase에서 보낸 메세지의 title
//        String message = remoteMessage.getNotification().getBody();//firebase에서 보낸 메세지의 내용
//        String test = remoteMessage.getData().get("FCM_PetDiary");
//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("FCM_PetDiary", test);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            String channel = "PetDiary";
//            String channel_nm = "PetDiary 새글 작성 시간";
//
//            NotificationManager notichannel = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
//                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
//            channelMessage.setDescription("새 게시글을 작성해주세요");
//            channelMessage.enableLights(true);
//            channelMessage.enableVibration(true);
//            channelMessage.setShowBadge(false);
//            channelMessage.setVibrationPattern(new long[]{1000, 1000});
//            notichannel.createNotificationChannel(channelMessage);
//
//            //푸시알림을 Builder를 이용하여 만듭니다.
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this, channel)
//                            .setSmallIcon(R.drawable.ic_launcher_background)
//                            .setContentTitle(title)//푸시알림의 제목
//                            .setContentText(message)//푸시알림의 내용
//                            .setChannelId(channel)
//                            .setAutoCancel(true)//선택시 자동으로 삭제되도록 설정.
//                            .setContentIntent(pendingIntent)//알림을 눌렀을때 실행할 인텐트 설정.
//                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(9999, notificationBuilder.build());
//
//        } else {
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this, "")
//                            .setSmallIcon(R.drawable.ic_launcher_background)
//                            .setContentTitle(title)
//                            .setContentText(message)
//                            .setAutoCancel(true)
//                            .setContentIntent(pendingIntent)
//                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(9999, notificationBuilder.build());
//
//        }
    }
}
