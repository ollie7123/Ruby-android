package com.jaydi.ruby;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.appspot.ruby_mine.rubymine.model.RubyzoneCol;
import com.appspot.ruby_mine.rubymine.model.User;
import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.database.DatabaseInter;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.location.tracking.TrackingService;
import com.jaydi.ruby.user.LocalUser;

public class LoadingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		initBackgroundTracking();
		getUserReady();
	}

	private void initBackgroundTracking() {
		NetworkInter.getRubyzones(new ResponseHandler<RubyzoneCol>() {

			@Override
			protected void onResponse(final RubyzoneCol rubyzoneCol) {
				if (rubyzoneCol == null || rubyzoneCol.getRubyzones() == null) {
					initLocationTracking();
					return;
				}

				DatabaseInter.deleteRubyzoneAll(RubyApplication.getInstance(), new ResponseHandler<Void>() {

					@Override
					protected void onResponse(Void res) {
						DatabaseInter.addRubyzones(RubyApplication.getInstance(), new ResponseHandler<Void>() {

							@Override
							protected void onResponse(Void res) {
								initLocationTracking();
							}

						}, rubyzoneCol.getRubyzones());
					}

				});
			}

			private void initLocationTracking() {
				// start location tracking
				Intent intent = new Intent(RubyApplication.getInstance(), TrackingService.class);
				startService(intent);
			}

		});
	}

	private void getUserReady() {
		if (!LocalUser.getReady())
			goToProfileDelayed();
		else
			refreshUser();
	}

	@SuppressLint("HandlerLeak")
	private void goToProfileDelayed() {
		new Handler() {

			@Override
			public void handleMessage(Message m) {
				Intent intent = new Intent(LoadingActivity.this, ProfileActivity.class);
				startActivity(intent);
			}

		}.sendEmptyMessageDelayed(0, 1000);
	}

	private void refreshUser() {
		NetworkInter.getUser(new ResponseHandler<User>() {

			@Override
			protected void onResponse(User res) {
				if (res != null) {
					LocalUser.setUser(res);
					goToMainDelayed();
				} else
					goToProfileDelayed();
			}

		}, LocalUser.getUser().getId());
	}

	@SuppressLint("HandlerLeak")
	private void goToMainDelayed() {
		new Handler() {

			@Override
			public void handleMessage(Message m) {
				Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
				startActivity(intent);
			}

		}.sendEmptyMessageDelayed(0, 1000);
	}

}
