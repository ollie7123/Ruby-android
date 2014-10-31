package com.jaydi.ruby.beacon;

import org.altbeacon.beacon.Identifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;

import com.appspot.ruby_mine.rubymine.model.Ruby;
import com.appspot.ruby_mine.rubymine.model.RubyCol;
import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.user.LocalUser;
import com.jaydi.ruby.utils.RubyUtils;

public class BeaconUpdateManager {

	public static void handleBeaconUpdate(Context context, Identifier id) {
		int state = 0;
		if (isScreenOn(context))
			state++;
		if (isAppOnScreen(context))
			state++;

		mineRuby(context, id, state);
	}

	private static boolean isScreenOn(Context context) {
		return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
	}

	private static boolean isAppOnScreen(Context context) {
		SharedPreferences pref = context.getSharedPreferences(RubyApplication.PREF_APP, Context.MODE_PRIVATE);
		return pref.getBoolean(RubyApplication.PROPERTY_ON_SCREEN, false);
	}

	private static void mineRuby(final Context context, Identifier id, final int state) {
		Ruby ruby = new Ruby();
		ruby.setUserId(LocalUser.getUser().getId());
		ruby.setPlanterId(Long.valueOf(id.toString()));

		NetworkInter.mineRuby(new ResponseHandler<RubyCol>(context.getMainLooper()) {

			@Override
			protected void onResponse(RubyCol res) {
				if (res == null || res.getUser() == null)
					return;

				LocalUser.setUser(res.getUser());
				onRubyFound(context, state, res);
			}

		}, ruby);
	}

	private static void onRubyFound(Context context, int state, RubyCol rubyCol) {
		switch (state) {
		case 0:
			RubyUtils.popupRuby(context, rubyCol);
			break;
		case 1:
			RubyUtils.notifyRuby(context, rubyCol);
			break;
		case 2:
			RubyUtils.toastRuby(context, rubyCol);
			break;
		}
	}

}
