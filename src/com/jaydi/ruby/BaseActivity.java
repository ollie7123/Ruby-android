package com.jaydi.ruby;

import org.altbeacon.beacon.Beacon;

import android.app.Activity;

import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.utils.ToastUtils;

public class BaseActivity extends Activity {
	@Override
	protected void onResume() {
		super.onResume();
		RubyApplication.getInstance().activityResumed(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		RubyApplication.getInstance().activityPaused(this);
	}

	public void toastRuby(final Beacon beacon) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ToastUtils.show("RubyMine: " + beacon.getId2());
			}

		});
	}
}
