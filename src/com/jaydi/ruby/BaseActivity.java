package com.jaydi.ruby;

import android.app.Activity;

import com.appspot.ruby_mine.rubymine.model.Rubymine;
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

	public void toastRuby(final Rubymine rubymine) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ToastUtils.show("found ruby in " + rubymine.getName());
			}

		});
	}
}
