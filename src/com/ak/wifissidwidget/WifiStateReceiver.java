package com.ak.wifissidwidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WifiStateReceiver extends BroadcastReceiver {
	
	private static final String LOG = "com.ak.wifissidwidget";

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.v(LOG, "WifiStateReceiver - onReceive()");
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
		
		String ssid = get_SSID(context);		
		
		/* TODO: remove Toast because WIFI states do change quite frequently */
		Toast.makeText(context, "WiFi - " + ssid, Toast.LENGTH_SHORT).show();
		
		remoteViews.setTextViewText(R.id.widget_textview, ssid);

		ComponentName thiswidget = new ComponentName(context, WifiSSIDWidgetAppWidgetProvider.class);		
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(thiswidget, remoteViews);

	}

	private String get_SSID(Context context) {
		
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

		return text_to_show;
		
	}

} 
