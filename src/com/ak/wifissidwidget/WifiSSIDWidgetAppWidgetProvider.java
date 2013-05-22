package com.ak.wifissidwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/* TODO:
 *  - force update of GUI once widget is added to home screen.
 *  - remove unnecessary permissions
 *  - remove unnecessary broadcast intents.
 *  - remove Toast calls (might get too annoying).
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
*
 *  Version 0.14.4 
 *  - replace icon (widget) - see:
 *  http://android-ui-utils.googlecode.com/hg/asset-studio/dist/icons-launcher.html#foreground.type=image&foreground.space.trim=1&foreground.space.pad=0.15&foreColor=fff%2C100&crop=0&backgroundShape=none&backColor=fff%2C100
 *  - for icon generation see also: http://makeappicon.com/

 *  Version 0.14.5  
 *  - smaller default size
 *  - allow resizing on android 3.0+
 *  
 *  Version 0.14.6    
 *  - forbid sd-card installation.
 *  
 *  Version 0.14.7  
 *  - reduced apk size by moving raw figures into "raw_media" directory.
 *  
 *  Version 0.15.0
 *  - fix bug where wifi ap is displayed as 'no connection' even though we are connected. Update problem?
 *  - listen to WIFI state changes and update string only then
 *  - update WIFI name display after we switch to different AP (android 2.3)
 *  - get rid of Timer and use BroadcastReceiver & UpdateWidgetService instead.
 *  - write AP name in Medium font size (changed also margins)
 *  - initialize AP name with 'wifi_not_initialized' string.
 *  - reduce minWidth by 6dp.
 *  
 *  Version 0.15.1
 *  - use xml resource to render button.
 */


/* WifiSSIDWidgetAppWidgetProvider will start the 'UpdateWidgetService' service */
public class WifiSSIDWidgetAppWidgetProvider extends AppWidgetProvider {

	private static final String LOG = "com.ak.wifissidwidget";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		// TODO: remove logging message
		Log.i(LOG, "onUpdate method called");
		
		// Get all ids
		ComponentName thisWidget = new ComponentName(context, WifiSSIDWidgetAppWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		// Update the widgets via the service
		context.startService(intent);
		
		// Update the current Wifi state
		/* does not really work
		String ssid = WifiStateReceiver.get_SSID(context.getApplicationContext());		
		
		RemoteViews remoteViews = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.main);
		AppWidgetManager manager = AppWidgetManager.getInstance(context.getApplicationContext());
		remoteViews.setTextViewText(R.id.widget_textview, ssid);
		manager.updateAppWidget(thisWidget, remoteViews);
		*/
		
	}
} 

