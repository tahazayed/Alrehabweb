package com.example.taha.alrehab.BackgroundServices;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;

import android.os.IBinder;
import android.util.Log;

import com.example.taha.alrehab.BusinessEntities.AlrehabNotification;
import com.example.taha.alrehab.JSON.AlrehabNotificationsJSONHandler;
import com.example.taha.alrehab.MainActivity;
import com.example.taha.alrehab.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.graphics.BitmapFactory;
import android.app.Notification;
import java.util.Random;

public class NotificationsService extends Service implements AlrehabNotificationsJSONHandler.AlrehabNotificationsJSONHandlerClient {

    private static final String TAG = "NotificationsService";

    private static long UPDATE_INTERVAL = 3 * 60 * 1000;  //default
    private static Timer timer = new Timer();
    private static Intent _intent;
    private boolean isRunning = false;
    private boolean IsDebug = true;
    @Override
    public void onCreate() {
        if (IsDebug) Log.d(TAG, "Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        _intent = intent;
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
            new AlrehabNotificationsJSONHandler(NotificationsService.this).execute(getString(R.string.NotificationAPI) + UUID.randomUUID().toString());
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
        Resources res = getApplicationContext().getResources();
        for (AlrehabNotification oAlrehabNotification : list) {
            Intent notificationIntent = new Intent(this,MainActivity.class);

            notificationIntent.putExtra("Type",(Integer.toString(oAlrehabNotification.get_type())));
            notificationIntent.putExtra("Id",(Integer.toString(oAlrehabNotification.get_id())));

            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                    0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE);



            NotificationManager nm = (NotificationManager) getApplicationContext()
                    .getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
          /*  Bundle extras=new Bundle();
            extras.putString("Type",(Integer.toString(oAlrehabNotification.get_type())));
            extras.putString("Id",(Integer.toString(oAlrehabNotification.get_id())));*/
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setTicker(oAlrehabNotification.get_title())
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(oAlrehabNotification.get_title())
                    .setContentText(oAlrehabNotification.get_title())
                    //.setExtras(extras)
                    .setOnlyAlertOnce(false)
                    .setGroup("Alrehab")
                    .setGroupSummary(true)
                    .setCategory("news");


            Notification n = builder.getNotification();

            n.defaults |= Notification.DEFAULT_ALL;
            nm.notify(rand.nextInt(), n);
        }



        /*Story[] lstStories = list.toArray(new Story[list.size()]);
        int size = lstStories.length;
        StoriesDBHandler db = new StoriesDBHandler(getApplicationContext());
        db.markAllStoriesDeleted();
        db.deleteStoriesMrkedDeleted();//test only
        for (Story story : lstStories) {
            Story oldStory = db.getStory(story.get_id());

            if (oldStory != null) {
                if (!oldStory.get_timestamp().equals(story.get_timestamp())) {
                    story.set_isbookmarked(oldStory.get_isbookmarked());
                    db.updateStory(story);
                }
                continue;
            }

            db.addStory(story);
        }
        db.deleteStoriesMrkedDeleted();*/


    }



}