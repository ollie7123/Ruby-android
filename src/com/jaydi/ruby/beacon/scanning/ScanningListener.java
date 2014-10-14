package com.jaydi.ruby.beacon.scanning;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.util.Log;

import com.jaydi.ruby.beacon.BeaconUpdateManager;

public class ScanningListener implements BeaconConsumer, RangeNotifier {
	private static final String UUID = "7ed18560-4686-43c7-a8bb-7621e22b1cc8";
	private static final String PREF_BEACON = "prefBeacon";

	private Context context;
	private BeaconManager beaconManager;
	private Set<Identifier> ids;

	@Override
	public Context getApplicationContext() {
		return context;
	}

	@Override
	public boolean bindService(Intent service, ServiceConnection conn, int flags) {
		return context.bindService(service, conn, flags);
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		context.unbindService(conn);
	}

	public void init(Context context) {
		this.context = context;
		beaconManager = BeaconManager.getInstanceForApplication(context);
		ids = new HashSet<Identifier>();

		// start beacon service
		beaconManager.bind(this);
		// set beacon layout to search any beacon profiles
		beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
	}

	public void term() {
		// stop beacon service
		beaconManager.unbind(this);
	}

	@Override
	public void onBeaconServiceConnect() {
		// attach range notifier interface which is this class
		beaconManager.setRangeNotifier(this);

		try {
			// start ranging beacons with name RubyMine
			// by setting unique UUID, detects only RubyMine beacons
			beaconManager.startRangingBeaconsInRegion(new Region("RubyMine", Identifier.parse(UUID), null, null));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
		checkRegion(beacons);
		countScanning();
	}

	private void checkRegion(Collection<Beacon> beacons) {
		// for visible beacons decide state by its distance and saved ids
		for (Beacon beacon : beacons) {
			if (!ids.contains(beacon.getId2())) {
				if (beacon.getDistance() < Double.valueOf(beacon.getId3().toString()))
					walkIn(beacon.getId2());
			} else {
				if (beacon.getDistance() > Double.valueOf(beacon.getId3().toString()))
					walkOut(beacon.getId2());
			}

			// log detected beacon
			ScanningLog.logBeacon(context, beacon);
		}

		// if saved beacons suddenly gets invisible, treat it as walk out
		List<Identifier> exitedIds = new ArrayList<Identifier>();
		for (Identifier id : ids) {
			boolean exists = false;
			for (Beacon beacon : beacons)
				if (beacon.getId2().equals(id))
					exists = true;

			if (!exists)
				exitedIds.add(id);
		}

		for (Identifier id : exitedIds)
			walkOut(id);
	}

	private void walkIn(Identifier id) {
		ids.add(id);

		// notify walk in if beacon id is not cached
		if (!isCachedBeacon(id)) {
			saveExpirationTime(id);
			BeaconUpdateManager.handleBeaconUpdate(context, id);
		}
	}

	private void walkOut(Identifier id) {
		ids.remove(id);

		// for crash protection
		if (ids.isEmpty())
			ScanningManager.forceFlush(context);
	}

	// for crash protection
	private int count = 0;

	// for crash protection
	private void countScanning() {
		if (++count % 1000 == 0)
			ScanningManager.forceFlush(context);
		Log.i("SL", "scan count: " + count);
	}

	private boolean isCachedBeacon(Identifier id) {
		long expirationTime = getExpirationTime(id);
		if (System.currentTimeMillis() < expirationTime)
			return true;
		else
			return false;
	}

	private long getExpirationTime(Identifier id) {
		SharedPreferences pref = getPref(context);
		return pref.getLong(id.toString(), 0);
	}

	private void saveExpirationTime(Identifier id) {
		// cache beacon detection, expiration time is the midnight of today
		SharedPreferences pref = getPref(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(id.toString(), getMidnightTime());
		editor.commit();
	}

	private long getMidnightTime() {
		Calendar c = Calendar.getInstance(Locale.KOREA);
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTimeInMillis();
	}

	private SharedPreferences getPref(Context context) {
		return context.getSharedPreferences(PREF_BEACON, Context.MODE_PRIVATE);
	}
}
