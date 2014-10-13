package com.jaydi.ruby.location;

import java.util.List;

import android.content.Context;
import android.location.Location;

import com.appspot.ruby_mine.rubymine.model.Rubyzone;
import com.jaydi.ruby.beacon.scanning.ScanningManager;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.database.DatabaseInter;
import com.jaydi.ruby.utils.CalUtils;

public class LocationUpdateManager {
	private static Location lastLocation;

	public static void handleLocationUpdate(Context context, Location location) {
		// save updated location, don't know if it will work as expected...
		lastLocation = location;

		// check if ruby zone is nearby
		getRubyZones(context, location);
	}

	private static void getRubyZones(final Context context, final Location location) {
		DatabaseInter.getRubyzones(context, new ResponseHandler<List<Rubyzone>>() {

			@Override
			protected void onResponse(List<Rubyzone> res) {
				checkNearbyRubyZone(context, location, res);
			}

		});
	}

	private static void checkNearbyRubyZone(Context context, Location location, List<Rubyzone> rubyZones) {
		// count if user is in zone
		int cnt = 0;
		for (Rubyzone rubyZone : rubyZones)
			if (CalUtils.calDistance(rubyZone.getLat(), rubyZone.getLng(), location.getLatitude(), location.getLongitude()) < rubyZone.getRange()) {
				cnt++;
				break;
			}

		// turn on ble if user is in zone
		if (cnt > 0)
			ScanningManager.turnOnScanning(context);
		// turn off ble if user is out of any zone
		else
			ScanningManager.turnOffScanning(context);
	}

	public static Location getLastLocation() {
		return lastLocation;
	}

}
