package com.jaydi.ruby;

import android.support.v4.app.FragmentActivity;

import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.utils.DialogUtils;
import com.jaydi.ruby.utils.ToastUtils;

public class BaseActivity extends FragmentActivity {
	@Override
	protected void onResume() {
		super.onResume();
		// report
		RubyApplication.getInstance().activityResumed(this);

		// check network
		if (!NetworkInter.isNetworkOnline())
			DialogUtils.networkAlert(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// report
		RubyApplication.getInstance().activityPaused(this);
	}

	public void toast(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ToastUtils.show(msg);
			}

		});
	}
}
