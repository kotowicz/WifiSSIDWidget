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

	@Override
	public void onReceive(Context context, Intent intent) {

		/* remove log */
		// Log.v(LOG, "WifiStateReceiver - onReceive()");
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);

		/* update the AP name */
		String ssid = WifiSSIDWidgetAppWidgetProvider.updateSSIDstring(context, remoteViews);
		
		/* remove Toast because WIFI states do change quite frequently */
		// Toast.makeText(context, "WiFi - " + ssid, Toast.LENGTH_SHORT).show();


		ComponentName thiswidget = new ComponentName(context, WifiSSIDWidgetAppWidgetProvider.class);		
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(thiswidget, remoteViews);

	}

} 
