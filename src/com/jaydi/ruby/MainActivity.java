package com.jaydi.ruby;

import java.util.List;

import android.location.Location;
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
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.database.DatabaseInter;
import com.jaydi.ruby.utils.CalUtils;
import com.jaydi.ruby.utils.ResourceUtils;

public class MainActivity extends BaseActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
	// constants for location updates
	public static final long UPDATE_INTERVAL = 3 * 1000;
	public static final long FASTEST_INTERVAL = 1 * 1000;
	public static final String NAV_COLLECT = "nav_collect";
	public static final String NAV_USE = "nav_collect";
	public static final String NAV_MYPAGE = "nav_mypage";

	private Rubyzone rubyzone;

	// location service variables
	private LocationClient locationClient;

	// ui variables
	private int navPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get location info to set current rubyzone
		initLocation();

		// prepare navigation menu and content fragments
		navPosition = 0;
		updateNavMenu();
		prepareFragments();
	}

	private void prepareFragments() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.frame_main_container_collect, new CollectRubyFragment());
		ft.add(R.id.frame_main_container_use, new UseRubyFragment());
		ft.add(R.id.frame_main_container_mypage, new MyPageFragment());
		ft.commit();
	}

	private void initLocation() {
		if (servicesConnected()) { // if google service available start fuse location service
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
		locationRequest.setInterval(UPDATE_INTERVAL);
		// set fastest interval
		locationRequest.setFastestInterval(FASTEST_INTERVAL);
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
				rubyzone.setId(2l);

				double distance = Double.MAX_VALUE;
				for (Rubyzone can : res)
					if (CalUtils.calDistance(lat, lng, can.getLat(), can.getLng()) < distance)
						rubyzone = can;

				MainActivity.this.rubyzone = rubyzone;
				refreshContents();
			}

		});
	}

	private void refreshContents() {
		changeRubyzoneTitle();
		loadChildContents();
	}

	private void changeRubyzoneTitle() {
		getActionBar().setTitle(ResourceUtils.getString(R.string.app_name) + " - " + rubyzone.getName());
	}

	private void loadChildContents() {
		((MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main_container_collect)).loadContents(rubyzone.getId());
		((MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main_container_use)).loadContents(rubyzone.getId());
		((MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main_container_mypage)).loadContents(rubyzone.getId());
	}

	public void changeNav(View view) {
		switch (view.getId()) {
		case R.id.button_main_nav_collect:
			navPosition = 0;
			break;
		case R.id.button_main_nav_use:
			navPosition = 1;
			break;
		case R.id.button_main_nav_mypage:
			navPosition = 2;
			break;
		default:
			navPosition = 0;
			break;
		}

		updateNavMenu();
		changeFragment();
	}

	private void updateNavMenu() {
		findViewById(R.id.button_main_nav_collect).setSelected(false);
		findViewById(R.id.button_main_nav_use).setSelected(false);
		findViewById(R.id.button_main_nav_mypage).setSelected(false);

		switch (navPosition) {
		case 0:
			findViewById(R.id.button_main_nav_collect).setSelected(true);
			break;
		case 1:
			findViewById(R.id.button_main_nav_use).setSelected(true);
			break;
		case 2:
			findViewById(R.id.button_main_nav_mypage).setSelected(true);
			break;
		default:
			findViewById(R.id.button_main_nav_collect).setSelected(true);
			break;
		}
	}

	private void changeFragment() {
		findViewById(R.id.frame_main_container_collect).setVisibility(View.GONE);
		findViewById(R.id.frame_main_container_use).setVisibility(View.GONE);
		findViewById(R.id.frame_main_container_mypage).setVisibility(View.GONE);

		switch (navPosition) {
		case 0:
			findViewById(R.id.frame_main_container_collect).setVisibility(View.VISIBLE);
			break;
		case 1:
			findViewById(R.id.frame_main_container_use).setVisibility(View.VISIBLE);
			break;
		case 2:
			findViewById(R.id.frame_main_container_mypage).setVisibility(View.VISIBLE);
			break;
		default:
			findViewById(R.id.frame_main_container_collect).setVisibility(View.VISIBLE);
			break;
		}
	}

}
