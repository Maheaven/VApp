package com.lody.virtual.client.stub;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;


/**
 * @author Lody
 */
public class DaemonService extends Service {

    private static final int NOTIFY_ID = 1001;

    public static void startup(Context context) {
        context.startService(new Intent(context, DaemonService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startup(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel(); // 新增：创建通知渠道
        startService(new Intent(this, InnerService.class));
        Notification notification =
                new Notification.Builder(this, "daemon_service_channel").setContentTitle("Daemon Service").setSmallIcon(android.R.drawable.ic_notification_overlay) // 设置图标资源
                .build();
        startForeground(NOTIFY_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public static final class InnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Notification notification =
                    new Notification.Builder(this, "daemon_service_channel").setContentTitle("Inner Service").setSmallIcon(android.R.drawable.ic_notification_overlay).build();
            startForeground(NOTIFY_ID, notification);
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "daemon_service_channel";
            String channelName = "Daemon Service Channel";
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

}
