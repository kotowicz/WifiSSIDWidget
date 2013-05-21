package com.ak.wifissidwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Broadcaster extends BroadcastReceiver {
	private static final String TAG = "WifiSSIDWidget";	
	private int state = 0;
	RemoteViews remoteViews;


	@Override
	public void onReceive(Context context, Intent intent) {

		Log.v(TAG, "onReceive()");

		String ssid = get_SSID(context); 
		state = state + 1;
		Toast.makeText(context, ssid + Integer.toString(state), Toast.LENGTH_LONG).show();

		Intent newintent = new Intent(context, UpdateWidgetService.class);
		String intent_string = "String you want to pass";
		newintent.putExtra("name", intent_string);
		context.startService(newintent);

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
