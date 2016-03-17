package com.taha.alrehab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.taha.alrehab.BackgroundServices.NotificationsService;

public class autostart extends BroadcastReceiver {
    public void onReceive(Context arg0, Intent arg1) {
        Intent intent = new Intent(arg0, NotificationsService.class);
        arg0.startService(intent);

    }
}