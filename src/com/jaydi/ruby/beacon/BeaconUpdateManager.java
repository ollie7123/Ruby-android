package com.jaydi.ruby.beacon;

import org.altbeacon.beacon.Identifier;

import android.content.Context;

import com.appspot.ruby_mine.rubymine.model.Ruby;
import com.appspot.ruby_mine.rubymine.model.RubyCol;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.user.LocalUser;
import com.jaydi.ruby.utils.PushUtils;

public class BeaconUpdateManager {

	public static void handleBeaconUpdate(Context context, Identifier id) {
		mineRuby(context, id);
	}

	private static void mineRuby(final Context context, Identifier id) {
		Ruby ruby = new Ruby();
		ruby.setUserId(LocalUser.getUser().getId());
		ruby.setPlanterId(Long.valueOf(id.toString()));

		NetworkInter.mineRuby(new ResponseHandler<RubyCol>(context.getMainLooper()) {

			@Override
			protected void onResponse(RubyCol res) {
				if (res == null || res.getUser() == null)
					return;

				LocalUser.setUser(res.getUser());
				onRubyFound(context, res);
			}

		}, ruby);
	}

	private static void onRubyFound(Context context, RubyCol rubyCol) {
		PushUtils.pushRuby(context, rubyCol);
	}

}
