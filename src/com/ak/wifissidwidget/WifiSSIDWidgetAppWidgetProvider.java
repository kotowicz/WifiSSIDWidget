package com.ak.wifissidwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/* Things left TODO:
 *
 *  - check widget with hidden SSID; what will be shown? nothing?
 *  - check maximum AP name length - scroll name if too long? 
 *  - better icon (store)
 *  - rethink UI / widget design.
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
 *
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
 *  
 *  Version 0.15.3
 *  - force update of GUI once widget is added to home screen.
 *  
 *  Version 0.15.4
 *  - fix launcher problems
 *  - update GUI on startup accordingly.
 *  
 *  Version 0.15.5
 *  - remove unnecessary permissions
 *  - remove unnecessary broadcast intents.
 *  - remove Toast calls (might get too annoying).
 *  - removed Log.i() calls.
 *  - removed unnecessary code.
 *  
 *  Version 0.15.6
 *  - added sample code for broadcasting PACKAGE_UPGRADE event. needs more work.
 *  
 *  Version 0.15.7
 *  - activated proguard -> app size reduced from 820KB to 236KB.
 *  
 *  Version 0.16.0
 *  - check for "0x" and "0X" strings returned by WifiManger
 *    see bug https://code.google.com/p/android/issues/detail?id=43336
 *  
 *  Version 0.16.5
 *  - reinitialize widget when 'PACKAGE_REPLACED' is broadcasted.
 *
 *  Version 0.16.6
 *  - use small font for header string -> button fits into widget.
 *
 *  Version 0.16.7
 *  - use layout_margin around button.
 *  
 *  Version 0.16.8
 *  - set explicit button height - needed on Android 2.x systems.
 *
 *  Version 0.16.9
 *  - remove stopSelf(); call
 *
 *  Version 0.17.0
 *  - use IntentService instead of Service class. This might make the UI more responsive.
 *
 *  Version 0.17.1 - release 52.
 *  - fix "rotation kills onClickListener" bug.
 *  - check for empty SSID string
 *
 *  Version 0.17.2 - release 53
 *  - same as 0.17.1 but using IntentService instead of Service.
 *
 *  Version 0.17.3 - release 54
 *  - check explicitly whether Wifi is connected when extracting SSID.
 *
 *  Version 0.17.4 - release 56
 *  - add permission android.permission.ACCESS_NETWORK_STATE".
 *  - use ConnectivityManager to check WiFi state.
 *
 *  Version 0.18.0 - release 57
 *  - add permission android.permission.ACCESS_COARSE_LOCATION
 *      - needed for Android >= 8.0 to read out SSID
 *  - update code to compile on android studio 3.2.1
 *      - works with gradle 4.6 and gradle plugin 3.2.1
 */


/* WifiSSIDWidgetAppWidgetProvider will start the 'UpdateWidgetService' service */
public class WifiSSIDWidgetAppWidgetProvider extends AppWidgetProvider {

    private static final String LOG = "com.ak.wifissidwidget";
    /* custom intent action */
    public static final String UPDATE_WIDGET = "com.ak.wifissidwidget.UPDATE_WIDGET";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // TODO: remove
        // Toast.makeText(context, "WifiSSIDWidgetAppWidgetProvider started", Toast.LENGTH_SHORT).show();

        // remove logging message
        // Log.i(LOG, "onUpdate() called");

        // Get all ids
        ComponentName thisWidget = new ComponentName(context, WifiSSIDWidgetAppWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        // Update the widgets via the service
        try {
            context.startService(intent);
        } catch (Exception e) {
            // catch for >= OREO
        }

        // remove logging message
        // Log.i(LOG, "Service started.");

    }


    public static String get_SSID(Context context) {

        String no_ssid = context.getString(R.string.no_ssid);
        String no_ssid_hex_lower = context.getString(R.string.no_ssid_hex_lower);
        String no_ssid_hex_upper = context.getString(R.string.no_ssid_hex_upper);
        String ssid = no_ssid;

        // default text to show in case of no connection.
        String text_to_show = context.getString(R.string.no_connection);

        // try / finally here so that we don't crash in case wifiManager is unavailable.
        try {

            // needs permission "android.permission.ACCESS_NETWORK_STATE"
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // networkInfo might be null.
            NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            // wifiManager & wifiInfo might be null.
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            // check if connected to WiFi.
            if (networkInfo != null && networkInfo.isConnected() == true) {

                ssid = wifiInfo.getSSID();
                // remove starting and ending '"' characters (if present)
                ssid = ssid.replaceAll("^\"|\"$", "");

                // look for the 'no_ssid' strings inside the 'ssid' string.
                // see bug https://code.google.com/p/android/issues/detail?id=43336
                //
                // DO not use '!=' for string comparison.
                boolean contains1 = ssid.contains(no_ssid);
                boolean contains2 = ssid.contains(no_ssid_hex_lower);
                boolean contains3 = ssid.contains(no_ssid_hex_upper);

                if ((ssid != null && ssid.length() != 0) && (contains1 == false) && (contains2 == false) && (contains3 == false)) {
                    text_to_show = ssid;
                }

            }
        } catch (Exception e) {
            // nothing to do in exception case.
            // TODO: remove me.
            // Toast.makeText(context, "Something bad happened: " + e, Toast.LENGTH_SHORT).show();
        }

        return text_to_show;

    }

    public static String updateSSIDstring(Context context, RemoteViews remoteViews){

        String ssid = WifiSSIDWidgetAppWidgetProvider.get_SSID(context);
        remoteViews.setTextViewText(R.id.widget_textview, ssid);

        return ssid;
    }

    public static RemoteViews updateUI(Context context) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);

        // Register an onClickListener
        Intent clickIntent = UpdateWidgetService.CreateWifiSettingsIntent(context.getApplicationContext());

        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);

        /* setup initial AP name value */
        WifiSSIDWidgetAppWidgetProvider.updateSSIDstring(context.getApplicationContext(), remoteViews);

        return remoteViews;

    }

} 

