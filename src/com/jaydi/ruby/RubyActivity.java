package com.jaydi.ruby;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.appspot.ruby_mine.rubymine.model.User;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.user.LocalUser;
import com.jaydi.ruby.utils.ResourceUtils;

public class RubyActivity extends Activity {
	public static final String EXTRA_RUBYMINE_NAME = "com.jaydi.ruby.extras.RUBYMINE_NAME";

	private String rubymineName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ruby);

		if (savedInstanceState == null)
			rubymineName = getIntent().getStringExtra(EXTRA_RUBYMINE_NAME);

		if (!((PowerManager) getSystemService(Context.POWER_SERVICE)).isScreenOn())
			turnOn();

		showRubyInfo();
		insertRuby();
	}

	private void turnOn() {
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}

	private void showRubyInfo() {
		TextView textInfo = (TextView) findViewById(R.id.text_ruby_message);
		textInfo.setText(String.format(ResourceUtils.getString(R.string.ruby_message), rubymineName));
	}

	private void insertRuby() {
		User user = LocalUser.getUser();
		user.setRuby(user.getRuby() + 1);

		NetworkInter.updateUser(new ResponseHandler<User>() {

			@Override
			protected void onResponse(User res) {
				if (res != null)
					LocalUser.setUser(res);
			}

		}, user);
	}

	public void goToMain(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
