package com.jaydi.ruby.location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.location.Location;

import com.appspot.ruby_mine.rubymine.model.Rubyzone;
import com.jaydi.ruby.beacon.scanning.ScanningManager;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.database.DatabaseInter;
import com.jaydi.ruby.utils.CalUtils;

public class LocationUpdateManager {
	private static Location lastLocation;
	private static Set<Long> idSet = new HashSet<Long>();

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

	private static void checkNearbyRubyZone(Context context, Location location, List<Rubyzone> rubyzones) {
		List<Long> ids = new ArrayList<Long>();
		for (Rubyzone rubyzone : rubyzones)
			if (CalUtils.calDistance(rubyzone.getLat(), rubyzone.getLng(), location.getLatitude(), location.getLongitude()) < rubyzone.getRange())
				ids.add(rubyzone.getId());

		for (Long id : ids)
			if (!idSet.contains(id)) {
				idSet.add(id);
				walkIn(context, id);
			}

		List<Long> outIds = new ArrayList<Long>();
		for (Long id : idSet)
			if (!ids.contains(id)) {
				outIds.add(id);
				walkOut(context, id);
			}
		idSet.removeAll(outIds);
	}

	private static void walkIn(Context context, Long id) {
		// save bluetooth state
		ScanningManager.saveBluetoothState(context);
		// turn on bluetooth
		ScanningManager.turnOffBluetooth();
		// turn on scanning if user is in zone
		ScanningManager.initScanningListener(context);
	}

	private static void walkOut(Context context, Long id) {
		// turn off scanning if user is out of any zone
		ScanningManager.termScanningListener();
		// turn off bluetooth if needed
		if (!ScanningManager.wasBluetoothOn(context))
			ScanningManager.turnOffBluetooth();
	}

	public static Location getLastLocation() {
		return lastLocation;
	}

}
