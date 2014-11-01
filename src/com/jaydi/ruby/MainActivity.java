package com.jaydi.ruby;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.appspot.ruby_mine.rubymine.model.Rubyzone;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.beacon.scanning.ScanningManager;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.database.DatabaseInter;
import com.jaydi.ruby.gcm.GcmManager;
import com.jaydi.ruby.utils.CalUtils;
import com.jaydi.ruby.utils.ResourceUtils;

public class MainActivity extends BaseActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
	private Rubyzone rubyzone;

	// location service variables
	private LocationClient locationClient;

	// ui variables
	private int navPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// prepare navigation menu and content fragments
		navPosition = 0;
		prepareFragments();
		updateNavMenu();
		changeFragment();
		loadChildContents();

		// check gcm
		GcmManager.check(this);
		// start beacon scanner
		initBeaconScanning();
		// get location info to set current rubyzone
		initLocation();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadChildContents();
	}

	private void prepareFragments() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.frame_main_container_mypage, new MyPageFragment());
		ft.add(R.id.frame_main_container_collect, new CollectRubyFragment());
		ft.add(R.id.frame_main_container_use, new UseRubyFragment());
		ft.add(R.id.frame_main_container_more, new MoreFragment());
		ft.commit();
	}

	public void changeNav(View view) {
		switch (view.getId()) {
		case R.id.button_main_nav_mypage:
			navPosition = 0;
			break;
		case R.id.button_main_nav_collect:
			navPosition = 1;
			break;
		case R.id.button_main_nav_use:
			navPosition = 2;
			break;
		case R.id.button_main_nav_more:
			navPosition = 3;
			break;
		default:
			navPosition = 0;
			break;
		}

		updateNavMenu();
		changeFragment();
		loadChildContents();
	}

	private void updateNavMenu() {
		findViewById(R.id.button_main_nav_mypage).setSelected(false);
		findViewById(R.id.button_main_nav_collect).setSelected(false);
		findViewById(R.id.button_main_nav_use).setSelected(false);
		findViewById(R.id.button_main_nav_more).setSelected(false);

		switch (navPosition) {
		case 0:
			findViewById(R.id.button_main_nav_mypage).setSelected(true);
			break;
		case 1:
			findViewById(R.id.button_main_nav_collect).setSelected(true);
			break;
		case 2:
			findViewById(R.id.button_main_nav_use).setSelected(true);
			break;
		case 3:
			findViewById(R.id.button_main_nav_more).setSelected(true);
			break;
		default:
			findViewById(R.id.button_main_nav_mypage).setSelected(true);
			break;
		}
	}

	private void changeFragment() {
		findViewById(R.id.frame_main_container_mypage).setVisibility(View.GONE);
		findViewById(R.id.frame_main_container_collect).setVisibility(View.GONE);
		findViewById(R.id.frame_main_container_use).setVisibility(View.GONE);
		findViewById(R.id.frame_main_container_more).setVisibility(View.GONE);

		switch (navPosition) {
		case 0:
			findViewById(R.id.frame_main_container_mypage).setVisibility(View.VISIBLE);
			break;
		case 1:
			findViewById(R.id.frame_main_container_collect).setVisibility(View.VISIBLE);
			break;
		case 2:
			findViewById(R.id.frame_main_container_use).setVisibility(View.VISIBLE);
			break;
		case 3:
			findViewById(R.id.frame_main_container_more).setVisibility(View.VISIBLE);
			break;
		default:
			findViewById(R.id.frame_main_container_mypage).setVisibility(View.VISIBLE);
			break;
		}
	}

	private void loadChildContents() {
		if (rubyzone == null)
			return;

		switch (navPosition) {
		case 0:
			((MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main_container_mypage)).loadContents(rubyzone.getId());
			break;
		case 1:
			((MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main_container_collect)).loadContents(rubyzone.getId());
			break;
		case 2:
			((MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main_container_use)).loadContents(rubyzone.getId());
			break;
		case 3:
			((MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main_container_more)).loadContents(rubyzone.getId());
			break;
		default:
			((MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main_container_mypage)).loadContents(rubyzone.getId());
			break;
		}
	}

	private void initBeaconScanning() {
		ScanningManager.turnOnBluetooth();
		ScanningManager.initScanningListener(RubyApplication.getInstance());
	}

	private void initLocation() {
		if (servicesConnected() && locationEnabled()) { // if google service available start fuse location service
			locationClient = new LocationClient(this, this, this);
			locationClient.connect();
		} else
			// else load links on temporary location
			setRubyzone(0d, 0d);
	}

	private boolean servicesConnected() {
		// check if google play service is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (ConnectionResult.SUCCESS == resultCode)
			return true;
		else
			return false;
	}

	private boolean locationEnabled() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	@Override
	public void onConnected(Bundle bundle) {
		requestLocationUpdate();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// retry
		locationClient.connect();
	}

	@Override
	public void onDisconnected() {
	}

	private void requestLocationUpdate() {
		// create location request object
		LocationRequest locationRequest = LocationRequest.create();
		// use power accuracy balanced priority
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// set interval
		locationRequest.setInterval(1000 * 3);
		// set fastest interval
		locationRequest.setFastestInterval(1000 * 1);
		locationClient.requestLocationUpdates(locationRequest, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		removeLocationUpdate();
		setRubyzone(location.getLatitude(), location.getLongitude());
	}

	private void removeLocationUpdate() {
		locationClient.removeLocationUpdates(this);
	}

	private void setRubyzone(final double lat, final double lng) {
		DatabaseInter.getRubyzones(this, new ResponseHandler<List<Rubyzone>>() {

			@Override
			protected void onResponse(List<Rubyzone> res) {
				Rubyzone rubyzone = new Rubyzone();
				double distance = Double.MAX_VALUE;

				for (Rubyzone can : res) {
					double d = CalUtils.calDistance(lat, lng, can.getLat(), can.getLng());
					if (d < distance && !can.getId().equals(1l)) {
						rubyzone = can;
						distance = d;
					}
				}

				MainActivity.this.rubyzone = rubyzone;
				getActionBar().setTitle(ResourceUtils.getString(R.string.app_name) + " - " + rubyzone.getName());
				loadChildContents();
			}

		});
	}

}
