package com.jaydi.ruby.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.jaydi.ruby.BaseActivity;

public class RubyApplication extends Application {
	public static final String PREF_APP = "prefApp";
	public static final String PROPERTY_ON_SCREEN = "propertyOnScreen";

	private static RubyApplication instance;
	private static BaseActivity onScreenActivity;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	@Override
	public void onTerminate() {
		instance = null;
		super.onTerminate();
	}

	public static RubyApplication getInstance() {
		return instance;
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
