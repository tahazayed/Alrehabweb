package com.taha.alrehab.BackgroundServices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.taha.alrehab.BusinessEntities.AlrehabNotification;
import com.taha.alrehab.DB.UserDBHandler;
import com.taha.alrehab.JSON.AlrehabNotificationsJSONHandler;
import com.taha.alrehab.MainActivity;
import com.taha.alrehab.R;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

public class NotificationsService extends Service implements AlrehabNotificationsJSONHandler.AlrehabNotificationsJSONHandlerClient {

    private static final String TAG = "NotificationsService";
    public static boolean isRunning = false;
    static String userId;
    private static long UPDATE_INTERVAL = 150 * 60 * 1000;  //default
    private static Timer timer = new Timer();
    private boolean IsDebug = false;

    @Override
    public void onCreate() {
        if (IsDebug) Log.d(TAG, "Service onCreate");

        isRunning = true;
        UserDBHandler db = new UserDBHandler(getApplicationContext());
        userId = db.getUserId();
        if (userId.isEmpty()) {
            userId = UUID.randomUUID().toString();
            db.updateUser(userId);
        }
        db.close();
        try {
            // Create a new console logger
            Logger logger = new Logger() {

                @Override
                public void log(String message, LogLevel level) {
                    if (IsDebug) Log.d(TAG, "NotificationsHub Log" + message);
                }
            };
            // Connect to the server
            HubConnection conn = new HubConnection(getString(R.string.NotificationsHUB), "", true, logger);

            // Create the hub proxy
            HubProxy proxy = conn.createHubProxy("NotificationsHub");

            proxy.subscribe(new Object() {
                @SuppressWarnings("unused")
                public void updateNotifications() {
                    if (IsDebug) Log.d(TAG, "UpdateNotifications called");
                    doServiceWork();
                }
            });
            // Start the connection
            conn.start()
                    .done(new Action<Void>() {

                        @Override
                        public void run(Void obj) throws Exception {
                            if (IsDebug) Log.d(TAG, "NotificationsHub Connected");
                        }
                    });
        } catch (Exception ex) {
        }

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (IsDebug) Log.d(TAG, "Service onStartCommand");

        timer.scheduleAtFixedRate(

                new TimerTask() {

                    public void run() {

                        doServiceWork();

                    }
                }, 1000, UPDATE_INTERVAL);
        if (IsDebug) Log.d(TAG, "Timer started....");

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        if (IsDebug) Log.d(TAG, "Service onBind");
        return null;
    }

    private void doServiceWork() {

        try {
            new AlrehabNotificationsJSONHandler(NotificationsService.this).execute(getString(R.string.NotificationAPI) + userId);
            if (IsDebug) Log.d(TAG, "StoriesJSONHandler invoked...");

        } catch (Exception e) {
            String error = e.getMessage();
        }

    }

    @Override
    public void onDestroy() {

        isRunning = false;

        if (timer != null) timer.cancel();
        if (IsDebug) Log.d(TAG, "Timer stopped...");

        if (IsDebug) Log.d(TAG, "Service onDestroy");
    }

    @Override
    public void onAlrehabNotificationsJSONHandlerClientResult(List<AlrehabNotification> list) {
        if (IsDebug)
            Log.d(TAG, "onAlrehabNotificationsJSONHandlerJSONHandlerClientResult invoked..." + list.size());


        final Random rand = new Random();
        rand.setSeed(100);
        if (list.size() > 0) {
            //NotificationManagerCompat.from(this).cancelAll();
            NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            Resources res = getApplicationContext().getResources();
            for (AlrehabNotification oAlrehabNotification : list) {
                int msgId = rand.nextInt();
                Intent notificationIntent = new Intent(this, MainActivity.class);

                notificationIntent.putExtra("Type", (Integer.toString(oAlrehabNotification.get_type())));
                notificationIntent.putExtra("Id", (Integer.toString(oAlrehabNotification.get_id())));

                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                        msgId, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                builder
                        //.addAction(R.mipmap.ic_launcher, oAlrehabNotification.get_title(), contentIntent)
                        .setContentIntent(contentIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setTicker(oAlrehabNotification.get_title())
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle(oAlrehabNotification.get_title())
                                //.setContentText(oAlrehabNotification.get_title())
                        .setContentText(oAlrehabNotification.get_body())
                                //.setExtras(extras)
                        .setOnlyAlertOnce(false)
                        .setGroup("Alrehab")
                        .setGroupSummary(false)
                        .setCategory("news");


                Notification n = builder.build();

                n.defaults |= Notification.DEFAULT_ALL;
                nm.notify(msgId, n);
            }
           /* Intent notificationIntent = new Intent(this, MainActivity.class);


            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                    0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationManager nm = (NotificationManager) this.getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setTicker("You Have : "+Integer.toString(list.size()))
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("Alrehab")
                    .setContentText("You Have : " + Integer.toString(list.size()))
                            //.setExtras(extras)
                    .setOnlyAlertOnce(false)
                    .setGroup("Alrehab")
                    .setGroupSummary(true)
                    .setCategory("news").build();


            Notification n = builder.getNotification();

            n.defaults |= Notification.DEFAULT_ALL;
            nm.notify(0, n);*/
        }


    }


}