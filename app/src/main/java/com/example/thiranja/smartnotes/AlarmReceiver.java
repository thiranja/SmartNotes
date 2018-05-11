package com.example.thiranja.smartnotes;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        //if (intent.getAction() != null && context != null){
            //if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
                // Setting the alarm comes with set alarrm even after boot up
                //Log.d(TAG, "onReceive: BOOT_COMPLETED");

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Notification notification = intent.getParcelableExtra(NOTIFICATION);
                int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
                notificationManager.notify(notificationId, notification);

            //}
        //}

    }
}
