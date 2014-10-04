package com.jaydi.ruby;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.TextView;

public class RubyActivity extends Activity {
	private int major;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ruby);

		if (!((PowerManager) getSystemService(Context.POWER_SERVICE)).isScreenOn())
			turnOn();

		if (savedInstanceState == null)
			major = getIntent().getIntExtra("com.jaydi.ruby.major", 0);

		showRubyInfo();
	}

	private void turnOn() {
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}

	private void showRubyInfo() {
		TextView textInfo = (TextView) findViewById(R.id.text_ruby_info);
		textInfo.setText("found Ruby from RubyMine:" + major);
	}

}
