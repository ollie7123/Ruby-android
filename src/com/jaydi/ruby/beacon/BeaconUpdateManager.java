package com.jaydi.ruby.beacon;

import org.altbeacon.beacon.Beacon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.utils.RubyUtils;

public class BeaconUpdateManager {

	public static void handleBeaconUpdate(Context context, Beacon beacon) {
		int state = 0;
		if (isScreenOn(context))
			state++;
		if (isAppOnScreen(context))
			state++;

		switch (state) {
		case 0:
			RubyUtils.popupRuby(context, beacon);
			break;
		case 1:
			RubyUtils.notifyRuby(context, beacon);
			break;
		case 2:
			RubyUtils.toastRuby(context, beacon);
			break;
		}

		// TODO
		getRubyMine(context, beacon);
	}

	private static boolean isScreenOn(Context context) {
		return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
	}

	private static boolean isAppOnScreen(Context context) {
		SharedPreferences pref = context.getSharedPreferences(RubyApplication.PREF_APP, Context.MODE_PRIVATE);
		return pref.getBoolean(RubyApplication.PROPERTY_ON_SCREEN, false);
	}

	private static void getRubyMine(Context context, Beacon beacon) {
		// TODO
		Log.i("BUM", "looper: " + (context.getMainLooper() != null));
	}

}
