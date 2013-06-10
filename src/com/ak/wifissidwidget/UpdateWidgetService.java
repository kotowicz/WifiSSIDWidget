package com.ak.wifissidwidget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.util.Log;

public class UpdateWidgetService extends IntentService {
    private static final String LOG = "com.ak.wifissidwidget";

    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        updateUI(intent);
    }


    private void updateUI(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        // remove log entry.
        // Log.i(LOG, "From Intent" + String.valueOf(allWidgetIds.length));

        // remove log entry and unnecessary code
		/*
		ComponentName thisWidget = new ComponentName(getApplicationContext(),
				WifiSSIDWidgetAppWidgetProvider.class);
		int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
		// Log.i(LOG, "Direct" + String.valueOf(allWidgetIds2.length));
		*/

        for (int widgetId : allWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.main);

            // Register an onClickListener
            Intent clickIntent = CreateWifiSettingsIntent(this.getApplicationContext());

            PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);

			/* setup initial AP name value */
            WifiSSIDWidgetAppWidgetProvider.updateSSIDstring(this.getApplicationContext(), remoteViews);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);

			/* remove log */
            // Log.i(LOG, "onClickListener Registered");

        }

		/* notify our system to send out a broadcast that will update the UI */
		/* this blocks the UI on Android 2.2! */
        // WifiSSIDWidgetAppWidgetProvider.sendUpdateIntent(this.getApplicationContext());

		/* TODO: why are we calling stopSelf() here? Clarify! */
        // removed in version 0.16.9 - testing.
        // stopSelf();
    }

/*	Deprecated code in case we want to extend "Service" class again.

    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {

        updateUI(intent);
		
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}*/

    public Intent CreateWifiSettingsIntent(Context context) {

        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");

        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(cn);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		/* remove log */
        // Log.i(LOG, "Intent created: com.android.settings.wifi.WifiSettings");

        return intent;

    }

} 