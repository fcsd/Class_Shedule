package derpyhooves.dipvlom.Adapters;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;

import derpyhooves.dipvlom.Activities.MainActivity;


public class AlarmAdapter extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isVibrateEnabled = prefs.getBoolean("vibrate", false);
        boolean isSoundEnabled = prefs.getBoolean("sound", false);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        if (isSoundEnabled) notification.defaults |= Notification.DEFAULT_SOUND;
        if (isVibrateEnabled) notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);

    }
}
