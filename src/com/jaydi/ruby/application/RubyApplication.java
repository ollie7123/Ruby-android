package com.jaydi.ruby.application;

import java.util.Collection;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import android.app.Application;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.jaydi.ruby.location.tracking.TrackingService;

public class RubyApplication extends Application implements BeaconConsumer, RangeNotifier {
	private static RubyApplication instance;

	private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initBeaconManager();
		initLocationTracker();
	}

	@Override
	public void onTerminate() {
		instance = null;
		termBeaconManager();
		super.onTerminate();
	}

	public static RubyApplication getInstance() {
		return instance;
	}

	private void initBeaconManager() {
		// start beacon scanner
		beaconManager.bind(this);
		// set beacon layout to search any beacon profiles
		beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
	}

	private void termBeaconManager() {
		beaconManager.unbind(this);
	}

	private void initLocationTracker() {
		// start location tracking service
		Intent intent = new Intent(this, TrackingService.class);
		startService(intent);
	}

	@Override
	public void onBeaconServiceConnect() {
		beaconManager.setRangeNotifier(this);

		try {
			// start ranging beacons with id RubyMine
			beaconManager.startRangingBeaconsInRegion(new Region("RubyMine", null, null, null));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
		// TODO just log beacon data for now
		for (Beacon beacon : beacons)
			Log.i("BEACON", beacon.getId1() + "/" + beacon.getId2() + "/" + beacon.getId3() + "/" + beacon.getDistance());
	}
}
