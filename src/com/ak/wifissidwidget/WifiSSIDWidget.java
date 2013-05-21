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
 *  - update WIFI name display after we switch to different AP (android 2.3)
 *  - better icon (store)
 *  
 *  DONE:
 *  - remove "" from SSID string on some android versions
 *  - create icon
 *  - fix background
 *  - create activity if widget is clicked -> go to Wifi settings
 *  - two row layout
 *  - check if 'unknown ssid' is found and replace with 'no connection'  
 *  - fix all warnings: "Window > Preferences > XML > XML Files > Validation.".  set “Indicate when no grammar is specified” option to “Ignore”.
 *  - fixed crash when WIFI is disabled  
 *  - use string resources instead of hard coded strings. 
 *  - replace icon (widget) - see:
 *  http://android-ui-utils.googlecode.com/hg/asset-studio/dist/icons-launcher.html#foreground.type=image&foreground.space.trim=1&foreground.space.pad=0.15&foreColor=fff%2C100&crop=0&backgroundShape=none&backColor=fff%2C100
 *  - for icon generation see also: http://makeappicon.com/  
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
		private Context context;


		public MySSIDDisplay(Context context, AppWidgetManager appWidgetManager) {
			this.appWidgetManager = appWidgetManager;
			// TODO: saving context here so that we have access to it inside the run() method.
			this.context = context;
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
			thisWidget = new ComponentName(context, WifiSSIDWidget.class);
		}

		@Override
		public void run() {
			
			//String no_ssid = "<unknown ssid>";
			String no_ssid = context.getString(R.string.no_ssid);
			String ssid = "";
			
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			// try / finally here so that we don't crash in case wifiManager is unavailable.
			try {
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				ssid = wifiInfo.getSSID();
				// remove starting and ending '"' characters (if present)
				ssid = ssid.replaceAll("^\"|\"$", "");				
			} catch (Exception e) {
				ssid = no_ssid;
			}

			// look for the 'no_ssid' string inside the 'ssid' string. DO not use '!=' for string comparison.
			boolean contains = ssid.contains(no_ssid);

			String text_to_show = context.getString(R.string.no_connection);
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
