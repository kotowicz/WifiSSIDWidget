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
    private final boolean DEBUG = false;

    @Override
    public void onReceive(Context context, Intent intent) {

		/* remove log */
        // Log.v(LOG, "WifiStateReceiver - onReceive()");
        // Log.v(LOG, "Action: " + intent.getAction());
        // if ("android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) {
        //	Log.v(LOG, "upgrading package");
        //}

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);

        // show update Toast only in debug mode.
        if (("android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) && DEBUG == true ) {
            Toast.makeText(context, "WifiSSIDWidget was updated - restarting", Toast.LENGTH_SHORT).show();
        }

        // show current SSID Toast only in debug mode.
        if (DEBUG == true) {
		    /* update the AP name */
            String ssid = WifiSSIDWidgetAppWidgetProvider.updateSSIDstring(context, remoteViews);
		
		    /* remove Toast because WIFI states do change quite frequently */
            Toast.makeText(context, "WiFi - " + ssid, Toast.LENGTH_SHORT).show();
        }

        ComponentName this_widget = new ComponentName(context, WifiSSIDWidgetAppWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(this_widget, remoteViews);

    }

} 
