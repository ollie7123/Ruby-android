package com.jaydi.ruby.gcm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);

		if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
			pushMessage();

		setResultCode(Activity.RESULT_OK);

		Log.i("pushedExtras : ", intent.getExtras().toString());
	}

	private void pushMessage() {
	}

}
