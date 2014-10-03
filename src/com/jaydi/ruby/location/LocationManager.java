package com.jaydi.ruby.location;

import android.location.Location;

public class LocationManager {
	private static Location lastLocation;

	public static void handleLocationUpdate(Location location) {
		// save updated location, don't know if it will work as expected...
		lastLocation = location;
	}

	public static Location getLastLocation() {
		return lastLocation;
	}

}
