package com.jaydi.ruby.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jaydi.ruby.BaseActivity;
import com.jaydi.ruby.beacon.scanning.BeaconListener;
import com.jaydi.ruby.connection.database.DatabaseInter;
import com.jaydi.ruby.location.tracking.TrackingService;
import com.jaydi.ruby.models.RubyZone;

public class RubyApplication extends Application {
	public static final String PREF_APP = "prefApp";
	public static final String PROPERTY_ON_SCREEN = "propertyOnScreen";

	private static RubyApplication instance;
	private static BaseActivity onScreenActivity;

	private BeaconListener beaconListener;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		// temporary
		insertTempZone();

		initBeaconManager();
		initLocationTracker();
	}

	// temporary
	private void insertTempZone() {
		RubyZone zone = new RubyZone();
		zone.setId(1);
		zone.setLat(37.505289d);
		zone.setLng(127.048323d);
		zone.setRange(100);

		DatabaseInter.addRubyZone(this, zone);
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
		// start scanning beacons
		beaconListener = new BeaconListener();
		beaconListener.init(this);
	}

	private void termBeaconManager() {
		// terminate beacon scanning
		if (beaconListener != null)
			beaconListener.term();
	}

	private void initLocationTracker() {
		// start location tracking service
		Intent intent = new Intent(this, TrackingService.class);
		startService(intent);
	}

	public void activityResumed(BaseActivity activity) {
		onScreenActivity = activity;
		SharedPreferences pref = getPref();
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(PROPERTY_ON_SCREEN, true);
		editor.commit();
	}

	public void activityPaused(BaseActivity activity) {
		onScreenActivity = null;
		SharedPreferences pref = getPref();
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(PROPERTY_ON_SCREEN, false);
		editor.commit();
	}

	public BaseActivity getOnScreenActivity() {
		return onScreenActivity;
	}

	private SharedPreferences getPref() {
		return getSharedPreferences(PREF_APP, MODE_PRIVATE);
	}

}
