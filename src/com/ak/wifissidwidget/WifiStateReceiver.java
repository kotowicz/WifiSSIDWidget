package com.ak.wifissidwidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WifiStateReceiver extends BroadcastReceiver {

    private static final String LOG = "com.ak.wifissidwidget";
    // TODO: set DEBUG to false for productive apps.
    private final boolean DEBUG = true;

    @Override
    public void onReceive(Context context, Intent intent) {

		/* remove log */
        // Log.v(LOG, "WifiStateReceiver - onReceive()");
        // Log.v(LOG, "Action: " + intent.getAction());
        // if ("android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) {
        //	Log.v(LOG, "upgrading package");
        //}

        // TODO: remove
        // Toast.makeText(context.getApplicationContext(), "onReceive in WifiStateReceiver() called", Toast.LENGTH_SHORT).show();

        RemoteViews remoteViews = WifiSSIDWidgetAppWidgetProvider.updateUI(context);

        // show update Toast only in debug mode.
        if ("android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) {
            if (DEBUG == true) {
                Toast.makeText(context, "WifiSSIDWidget was updated - restarting", Toast.LENGTH_SHORT).show();
            }
        }

        // show current SSID Toast only in debug mode because WIFI states do change quite frequently
        if (DEBUG == true) {
            String ssid = WifiSSIDWidgetAppWidgetProvider.get_SSID(context);
            Toast.makeText(context, "WiFi - " + ssid, Toast.LENGTH_SHORT).show();
        }

        // Push update for this widget to the home screen
        ComponentName this_widget = new ComponentName(context, WifiSSIDWidgetAppWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(this_widget, remoteViews);

    }

} 
