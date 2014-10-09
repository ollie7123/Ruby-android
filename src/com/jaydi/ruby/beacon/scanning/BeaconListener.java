package com.jaydi.ruby.beacon.scanning;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

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

public class BeaconListener implements BeaconConsumer, RangeNotifier {
	private static final String UUID = "7ed18560-4686-43c7-a8bb-7621e22b1cc8";
	private static final String PREF_BEACON = "prefBeacon";

	private static Context context;
	private static BeaconManager beaconManager;

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
		BeaconListener.context = context;
		beaconManager = BeaconManager.getInstanceForApplication(context);

		// start beacon scanner
		beaconManager.bind(this);
		// set beacon layout to search any beacon profiles
		beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
	}

	public void term() {
		beaconManager.unbind(this);
	}

	@Override
	public void onBeaconServiceConnect() {
		beaconManager.setRangeNotifier(this);

		try {
			// start ranging beacons with id RubyMine
			beaconManager.startRangingBeaconsInRegion(new Region("RubyMine", Identifier.parse(UUID), null, null));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
		Log.i("BEACON", "FOUND: " + beacons.size());
		for (Beacon beacon : beacons)
			processBeacon(beacon);
	}

	private void processBeacon(Beacon beacon) {
		if (filtBeacon(beacon)) {
			saveExpirationTime(beacon);
			BeaconUpdateManager.handleBeaconUpdate(context, beacon);
		}
	}

	private boolean filtBeacon(Beacon beacon) {
		long expirationTime = getExpirationTime(beacon);
		if (new Date().getTime() > expirationTime)
			return true;
		else
			return false;
	}

	private long getExpirationTime(Beacon beacon) {
		SharedPreferences pref = getPref(context);
		return pref.getLong(beacon.getId2().toString(), 0);
	}

	private void saveExpirationTime(Beacon beacon) {
		SharedPreferences pref = getPref(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(beacon.getId2().toString(), getMidnightTime());
		editor.commit();
	}

	private long getMidnightTime() {
		Calendar c = Calendar.getInstance(Locale.KOREA);
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 24);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis();
	}

	private SharedPreferences getPref(Context context) {
		return context.getSharedPreferences(PREF_BEACON, Context.MODE_PRIVATE);
	}
}
