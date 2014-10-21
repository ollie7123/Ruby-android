package com.jaydi.ruby;

import android.support.v4.app.FragmentActivity;

import com.appspot.ruby_mine.rubymine.model.Rubymine;
import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.utils.DialogUtils;
import com.jaydi.ruby.utils.ResourceUtils;
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

	public void toastRuby(final Rubymine rubymine) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ToastUtils.show(String.format(ResourceUtils.getString(R.string.ruby_message), rubymine.getName()));
			}

		});
	}
}
