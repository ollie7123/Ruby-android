package com.jaydi.ruby.beacon;

import org.altbeacon.beacon.Beacon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;

import com.appspot.ruby_mine.rubymine.model.Rubymine;
import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.utils.RubyUtils;

public class BeaconUpdateManager {

	public static void handleBeaconUpdate(Context context, Beacon beacon) {
		int state = 0;
		if (isScreenOn(context))
			state++;
		if (isAppOnScreen(context))
			state++;

		getRubyMine(context, beacon, state);
	}

	private static void getRubyMine(final Context context, final Beacon beacon, final int state) {
		NetworkInter.getRubymine(new ResponseHandler<Rubymine>(context.getMainLooper()) {

			@Override
			protected void onResponse(Rubymine res) {
				if (res == null)
					return;

				onRubymineFound(context, state, res);
			}

		}, Long.valueOf(beacon.getId2().toString()));
	}

	private static void onRubymineFound(Context context, int state, Rubymine rubymine) {
		switch (state) {
		case 0:
			RubyUtils.popupRuby(context, rubymine);
			break;
		case 1:
			RubyUtils.notifyRuby(context, rubymine);
			break;
		case 2:
			RubyUtils.toastRuby(context, rubymine);
			break;
		}
	}

	private static boolean isScreenOn(Context context) {
		return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
	}

	private static boolean isAppOnScreen(Context context) {
		SharedPreferences pref = context.getSharedPreferences(RubyApplication.PREF_APP, Context.MODE_PRIVATE);
		return pref.getBoolean(RubyApplication.PROPERTY_ON_SCREEN, false);
	}

}
