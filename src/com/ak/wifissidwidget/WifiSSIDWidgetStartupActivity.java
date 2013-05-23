package com.ak.wifissidwidget;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class WifiSSIDWidgetStartupActivity extends Activity {

	private static final String LOG = "com.ak.wifissidwidget";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG, "onCreate() StartupActivity called");		
		WifiSSIDWidgetAppWidgetProvider.sendUpdateIntent(this.getApplicationContext());

	}	
	
}
