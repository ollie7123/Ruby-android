package com.jaydi.ruby.location;

import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.Location;

import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.database.DatabaseInter;
import com.jaydi.ruby.models.RubyZone;
import com.jaydi.ruby.utils.CalUtils;

public class LocationUpdateManager {
	private static Location lastLocation;
	// flag if original status was turned-off
	private static boolean turnOnFlag;

	public static void handleLocationUpdate(Context context, Location location) {
		// save updated location, don't know if it will work as expected...
		lastLocation = location;

		// check if ruby zone is nearby
		getRubyZones(context, location);
	}

	private static void getRubyZones(Context context, final Location location) {
		DatabaseInter.getRubyZones(context, new ResponseHandler<List<RubyZone>>() {

			@Override
			protected void onResponse(List<RubyZone> res) {
				checkNearbyRubyZone(res, location);
			}

		});
	}

	private static void checkNearbyRubyZone(List<RubyZone> rubyZones, Location location) {
		// count if user is in zone
		int cnt = 0;
		for (RubyZone rubyZone : rubyZones)
			if (CalUtils.calDistance(rubyZone.getLat(), rubyZone.getLng(), location.getLatitude(), location.getLongitude()) < rubyZone.getRange())
				cnt++;

		// turn on ble if user is in zone
		if (cnt > 0)
			turnOnBLE();
		// turn off ble if original ble status was turned-off
		else if (turnOnFlag)
			turnOffBLE();
	}

	private static void turnOnBLE() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (!adapter.isEnabled())
			turnOnFlag = adapter.enable();
		else
			turnOnFlag = false;
	}

	private static void turnOffBLE() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter.isEnabled())
			adapter.disable();
		turnOnFlag = false;
	}

	public static Location getLastLocation() {
		return lastLocation;
	}

}
