package com.chorchuri.app.util.download;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.downloader.PRDownloader;

public class DownloadPauseReceiver extends BroadcastReceiver {

    public static final String EXTRA_NOTIFICATION_ID = "NOTIFICATION_ID";

    @Override
    public void onReceive(Context context, Intent intent) {

        int adminVideoId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1);
        if (adminVideoId != -1) {
            PRDownloader.pause(adminVideoId);
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify();
        }
    }
}

