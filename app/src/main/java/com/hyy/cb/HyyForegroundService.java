package com.hyy.cb;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class HyyForegroundService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "my_channel_id";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 创建通知通道
        createNotificationChannel();
        // 创建一个通知
        Notification notification = createNotification();
        // 将服务设置为前台服务
        startForeground(NOTIFICATION_ID, notification);
        // 返回 START_STICKY 以确保服务在被系统杀死后能够重新启动
        return START_STICKY;
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel Name", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
    }

    private Notification createNotification() {
        // 创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My App is Running")
                .setContentText("Your app is running in the background.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true); // 使通知成为常驻通知

        // 创建一个 PendingIntent，以便用户点击通知时打开你的应用
        Intent intent = new Intent(this, MainActivity.class); // 替换为你的入口 Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);

        return builder.build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 停止前台服务并移除通知
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
