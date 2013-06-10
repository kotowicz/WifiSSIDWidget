package com.ak.wifissidwidget;

import android.app.IntentService;
//import android.os.IBinder;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.app.PendingIntent;
import android.util.Log;
import android.widget.Toast;

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

        // TODO: remove
        // Toast.makeText(this.getApplicationContext(), "updateUI() called", Toast.LENGTH_SHORT).show();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {

            RemoteViews remoteViews = WifiSSIDWidgetAppWidgetProvider.updateUI(this.getApplicationContext());
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

	/* Deprecated code in case we want to extend "Service" class again.

    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {

        // TODO: remove
        // Toast.makeText(this.getApplicationContext(), "onStartCommand started", Toast.LENGTH_SHORT).show();

        updateUI(intent);
        stopSelf();

        // START_* values only indicate to the system what it should do if, for some reason, the system decides to kill the Service.
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}*/

    public static Intent CreateWifiSettingsIntent(Context context) {

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