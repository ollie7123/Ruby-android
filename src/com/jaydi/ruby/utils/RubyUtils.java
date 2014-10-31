package com.jaydi.ruby.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.appspot.ruby_mine.rubymine.model.Ruby;
import com.appspot.ruby_mine.rubymine.model.RubyCol;
import com.appspot.ruby_mine.rubymine.model.Rubymine;
import com.jaydi.ruby.BaseActivity;
import com.jaydi.ruby.MainActivity;
import com.jaydi.ruby.R;
import com.jaydi.ruby.RecommendActivity;
import com.jaydi.ruby.RubyActivity;
import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.models.RubyParcel;
import com.jaydi.ruby.models.RubymineParcel;

public class RubyUtils {

	public static void popupRuby(Context context, RubyCol rubyCol) {
		Intent intent = new Intent(context, RubyActivity.class);
		setExtras(intent, rubyCol);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);

		notifyRuby(context, rubyCol);

		vibrate();
	}

	public static void notifyRuby(Context context, RubyCol rubyCol) {
		Intent intent = new Intent(context, RubyActivity.class);
		setExtras(intent, rubyCol);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setAutoCancel(true);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(ResourceUtils.getString(R.string.app_name));
		builder.setContentText(String.format(ResourceUtils.getString(R.string.ruby_message), rubyCol.getPlanter().getName()));
		builder.setContentIntent(resultPendingIntent);

		NotificationManager notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notiManager.notify(rubyCol.getPlanter().getId().intValue(), builder.build());

		vibrate();
	}

	private static void setExtras(Intent intent, RubyCol rubyCol) {
		List<RubyParcel> rubyParcels = new ArrayList<RubyParcel>();
		for (Ruby ruby : rubyCol.getRubies())
			rubyParcels.add(new RubyParcel(ruby));

		List<RubymineParcel> giverParcels = new ArrayList<RubymineParcel>();
		for (Rubymine giver : rubyCol.getGivers())
			giverParcels.add(new RubymineParcel(giver));

		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(RecommendActivity.EXTRA_RUBIES, rubyParcels.toArray());
		intent.putExtra(RecommendActivity.EXTRA_PLANTER, new RubymineParcel(rubyCol.getPlanter()));
		intent.putExtra(RecommendActivity.EXTRA_GIVERS, giverParcels.toArray());
	}

	public static void toastRuby(Context context, RubyCol rubyCol) {
		BaseActivity activity = RubyApplication.getInstance().getOnScreenActivity();
		if (activity != null)
			activity.toastRuby(rubyCol.getPlanter());
		notifyRuby(context, rubyCol);

		vibrate();
	}

	public static void vibrate() {
		Vibrator vibrator = (Vibrator) RubyApplication.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(600);
	}
}
