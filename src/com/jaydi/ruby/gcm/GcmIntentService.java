package com.jaydi.ruby.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.appspot.ruby_mine.rubymine.model.User;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jaydi.ruby.user.LocalUser;
import com.jaydi.ruby.utils.PushUtils;

public class GcmIntentService extends IntentService {
	private static final String MESSAGE_KEY = "MESSAGE_KEY";
	private static final String LEVEL_CHANGE_STATE = "LEVEL_CHANGE_STATE";

	private static final String KEY_LEVEL_CHANGE = "KEY_LEVEL_CHANGE";

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				handleExtras(extras);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void handleExtras(Bundle extras) {
		Log.i("GCM", "extras: " + extras);

		String key = extras.getString(MESSAGE_KEY);
		if (key.equals(KEY_LEVEL_CHANGE)) {
			boolean change = Boolean.valueOf(extras.getString(LEVEL_CHANGE_STATE));
			User user = LocalUser.getUser();
			if (change)
				user.setLevel(user.getLevel() + 1);
			else
				user.setLevel(user.getLevel() - 1);
			LocalUser.setUser(user);

			PushUtils.pushLevelChange(this, change);
		}
	}

}
