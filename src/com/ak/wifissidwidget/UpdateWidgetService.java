package com.ak.wifissidwidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	private static final String LOG = "com.ak.wifissidwidget";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// TODO: remove log entry.
		Log.i(LOG, "onStartCommand() Called");

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		
		// TODO: remove log entry.
		Log.i(LOG, "From Intent" + String.valueOf(allWidgetIds.length));

		for (int widgetId : allWidgetIds) {

			RemoteViews remoteViews = new RemoteViews(this
					.getApplicationContext().getPackageName(),
					R.layout.main);			

			// Register an onClickListener
			Intent clickIntent = CreateWifiSettingsIntent(this.getApplicationContext());

			PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);
			
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
		stopSelf();

		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public Intent CreateWifiSettingsIntent(Context context) {
		
		final Intent intent = new Intent(Intent.ACTION_MAIN, null);
		final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
		
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(cn);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Log.i(LOG, "Intent created: com.android.settings.wifi.WifiSettings");
		
		return intent;
		
	}   	
	
} 