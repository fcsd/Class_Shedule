package derpyhooves.dipvlom.Adapters;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import derpyhooves.dipvlom.Activities.MainActivity;


public class AlarmAdapter extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        notification.defaults = Notification.DEFAULT_ALL;


        int id = intent.getIntExtra(NOTIFICATION_ID, 0);

        notificationManager.notify(id, notification);

    }
}
