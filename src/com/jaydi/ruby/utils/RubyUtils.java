package com.jaydi.ruby.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.appspot.ruby_mine.rubymine.model.Rubymine;
import com.jaydi.ruby.BaseActivity;
import com.jaydi.ruby.MainActivity;
import com.jaydi.ruby.R;
import com.jaydi.ruby.RubyActivity;
import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.models.RubymineParcel;

public class RubyUtils {

	public static void popupRuby(Context context, Rubymine rubymine) {
		Intent intent = new Intent(context, RubyActivity.class);
		intent.putExtra(RubyActivity.EXTRA_RUBYMINE, new RubymineParcel(rubymine));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);

		notifyRuby(context, rubymine);

		vibrate();
	}

	public static void notifyRuby(Context context, Rubymine rubymine) {
		Intent intent = new Intent(context, RubyActivity.class);
		intent.putExtra(RubyActivity.EXTRA_RUBYMINE, new RubymineParcel(rubymine));

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setAutoCancel(true);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(ResourceUtils.getString(R.string.app_name));
		builder.setContentText(String.format(ResourceUtils.getString(R.string.ruby_message), rubymine.getName()));
		builder.setContentIntent(resultPendingIntent);

		NotificationManager notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notiManager.notify(rubymine.getId().intValue(), builder.build());

		vibrate();
	}

	public static void toastRuby(Context context, Rubymine rubymine) {
		BaseActivity activity = RubyApplication.getInstance().getOnScreenActivity();
		if (activity != null)
			activity.toastRuby(rubymine);
		notifyRuby(context, rubymine);

		vibrate();
	}

	public static void vibrate() {
		Vibrator vibrator = (Vibrator) RubyApplication.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(600);
	}
}
