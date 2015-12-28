package com.example.taha.alrehab.BackgroundServices;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.taha.alrehab.BusinessEntities.AlrehabNotification;
import com.example.taha.alrehab.JSON.AlrehabNotificationsJSONHandler;
import com.example.taha.alrehab.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

//import com.alrehablife.alrehab.DB.StoriesDBHandler;

public class NotificationsService extends Service implements AlrehabNotificationsJSONHandler.AlrehabNotificationsJSONHandlerClient {

    private static final String TAG = "NotificationsService";
    public static NotificationManager nm;
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
       /* PendingIntent contentIntent = PendingIntent.getActivity(_intent,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) _intent
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = _intent.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(_intent);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.app_icon))
                .setTicker(payload)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Message")
                .setContentText(payload);
        Notification n = builder.getNotification();

        n.defaults |= Notification.DEFAULT_ALL;
        nm.notify(0, n);
*/
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