package com.ak.wifissidwidget;

public class DeprecatedAndNotUsed {

}


/*

public class WifiSSIDWidget extends BroadcastReceiver {
	private static final String TAG = "WifiSSIDWidget";	
	private int state = 0;
	RemoteViews remoteViews;

	public class MyWidgetProvider extends AppWidgetProvider {

		  private static final String LOG = "de.vogella.android.widget.example";

		  @Override
		  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
		      int[] appWidgetIds) {

		    Log.w(LOG, "onUpdate method called");
		    // Get all ids
		    ComponentName thisWidget = new ComponentName(context,
		        MyWidgetProvider.class);
		    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		    // Build the intent to call the service
		    Intent intent = new Intent(context.getApplicationContext(),
		        UpdateWidgetService.class);
		    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		    // Update the widgets via the service
		    context.startService(intent);
		  }
		} 
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	Log.v(TAG, "onReceive()");

    	String ssid = get_SSID(context); 
    	state = state + 1;
        Toast.makeText(context, ssid + Integer.toString(state), Toast.LENGTH_LONG).show();
        
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
        remoteViews.setTextViewText(R.id.widget_textview, ssid);

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

} */

/*
public class WifiSSIDWidget extends AppWidgetProvider {
	private static final String TAG = "WifiSSIDWidget";
	public static final String ACTION_BUTTON1_CLICKED = "com.ak.wifissidwidget.BUTTON1_CLICKED";


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		// Get all ids
	    ComponentName thisWidget = new ComponentName(context, WifiSSIDWidget.class);
	    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
	    for (int widgetId : allWidgetIds) {		
		
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
		if (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
			//Do something
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

			 register button click 
			Intent intent = new Intent(ACTION_BUTTON1_CLICKED);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);

			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}

	} 

}*/