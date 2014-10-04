package com.jaydi.ruby.utils;

import org.altbeacon.beacon.Beacon;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.jaydi.ruby.BaseActivity;
import com.jaydi.ruby.MainActivity;
import com.jaydi.ruby.R;
import com.jaydi.ruby.RubyActivity;
import com.jaydi.ruby.application.RubyApplication;

public class RubyUtils {

	public static void popupRuby(Context context, Beacon beacon) {
		Intent intent = new Intent(context, RubyActivity.class);
		intent.putExtra("com.jaydi.ruby.major", Integer.valueOf(beacon.getId2().toString()));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);

		vibrate();
	}

	public static void notifyRuby(Context context, Beacon beacon) {
		Intent intent = new Intent(context, RubyActivity.class);
		intent.putExtra("com.jaydi.ruby.major", Integer.valueOf(beacon.getId2().toString()));

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setAutoCancel(true);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle("RubyMine");
		builder.setContentText("Found Ruby");
		builder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(Integer.valueOf(beacon.getId2().toString()), builder.build());

		vibrate();
	}

	public static void toastRuby(Context context, Beacon beacon) {
		BaseActivity activity = RubyApplication.getInstance().getOnScreenActivity();
		if (activity != null)
			activity.toastRuby(beacon);
		else
			notifyRuby(context, beacon);

		vibrate();
	}

	public static void vibrate() {
		Vibrator vibrator = (Vibrator) RubyApplication.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(600);
	}
}
