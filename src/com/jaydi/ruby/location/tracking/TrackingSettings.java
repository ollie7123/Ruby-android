package com.jaydi.ruby.location.tracking;

import android.content.Context;
import android.content.SharedPreferences;

public class TrackingSettings {
	public static void setOnRequest(Context context, boolean onRequest) {
		SharedPreferences prefs = context.getSharedPreferences("LocationTrackingPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("OnRequest", onRequest);
		editor.commit();
	}
	
	public static boolean isOnRequest(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("LocationTrackingPrefs", Context.MODE_PRIVATE);
		return prefs.getBoolean("OnRequest", false);
	}
}
