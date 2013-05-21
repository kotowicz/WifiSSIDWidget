package com.ak.wifissidwidget;

import java.util.Timer;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.app.PendingIntent;

/* TODO:
 *  - listen to WIFI state changes and update string only then
 *  - fix all warnings
 *  - better icon
 *  
 *  DONE:
 *  - remove "" from SSID string on some android versions
 *  - create icon
 *  - fix background
 *  - create activity if widget is clicked -> go to Wifi settings
 *  - two row layout
 *  - check if 'unknown ssid' is found and replace with 'no connection'  
 */


public class WifiSSIDWidget extends AppWidgetProvider {
	private static final String TAG = "WifiSSIDWidget";
	public static final String ACTION_BUTTON1_CLICKED = "com.ak.wifissidwidget.BUTTON1_CLICKED";


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MySSIDDisplay(context, appWidgetManager), 1, 1000);
		Log.v(TAG, "onUpdate()");
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		//Log.d(TAG, "onReceive() " + intent.getAction());
		super.onReceive(context, intent);

		if (ACTION_BUTTON1_CLICKED.equals(intent.getAction()))
		{
			// Log.d(TAG, "onReceive() - button clicked");
			openWifiSettings(context);
		}	
	}

	public void openWifiSettings(Context context) {
		final Intent intent = new Intent(Intent.ACTION_MAIN, null);
		final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
		
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(cn);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(intent);
	}   	

	private class MySSIDDisplay extends TimerTask {
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		private WifiManager wifiManager;
		private Context context;


		public MySSIDDisplay(Context context, AppWidgetManager appWidgetManager) {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			this.wifiManager = wifiManager;
			this.appWidgetManager = appWidgetManager;
			// TODO: saving context here so that we have access to it inside the run() method.
			this.context = context;
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
			thisWidget = new ComponentName(context, WifiSSIDWidget.class);
		}

		@Override
		public void run() {
			WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();	
			String ssid = wifiInfo.getSSID();
			// remove starting and ending '"' characters (if present)
			ssid = ssid.replaceAll("^\"|\"$", "");
			
			// look for the 'no_ssid' string inside the 'ssid' string. DO not use '!=' for string comparison.
			String no_ssid = "<unknown ssid>";
			boolean contains = ssid.contains(no_ssid);

			String text_to_show = "No connection";
			if ((ssid != null) && (contains == false)) {
				text_to_show = ssid;
			}
			remoteViews.setTextViewText(R.id.widget_textview, text_to_show);

			/* register button click */
			Intent intent = new Intent(ACTION_BUTTON1_CLICKED);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);

			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}

	} 

}
