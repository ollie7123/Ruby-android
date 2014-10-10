package com.jaydi.ruby.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.appspot.ruby_mine.rubymine.model.RubyzoneCol;
import com.jaydi.ruby.BaseActivity;
import com.jaydi.ruby.beacon.scanning.BeaconListener;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.database.DatabaseInter;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.location.tracking.TrackingService;

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

		initBackgroundFeatures();
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

	private void initBackgroundFeatures() {
		NetworkInter.getRubyzones(new ResponseHandler<RubyzoneCol>() {

			@Override
			protected void onResponse(final RubyzoneCol rubyzoneCol) {
				if (rubyzoneCol != null && rubyzoneCol.getRubyzones() != null)
					DatabaseInter.deleteRubyzoneAll(RubyApplication.this, new ResponseHandler<Void>() {

						@Override
						protected void onResponse(Void res) {
							DatabaseInter.addRubyzones(RubyApplication.this, new ResponseHandler<Void>() {

								@Override
								protected void onResponse(Void res) {
									initBeaconManager();
									initLocationTracker();
								}

							}, rubyzoneCol.getRubyzones());
						}

					});
				else {
					initBeaconManager();
					initLocationTracker();
				}
			}

		});
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
