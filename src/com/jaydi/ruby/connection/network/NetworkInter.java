package com.jaydi.ruby.connection.network;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.appspot.ruby_mine.rubymine.Rubymine;
import com.appspot.ruby_mine.rubymine.model.Coupon;
import com.appspot.ruby_mine.rubymine.model.User;
import com.appspot.ruby_mine.rubymine.model.Userpair;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.jaydi.ruby.R;
import com.jaydi.ruby.application.Cache;
import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.threading.ImageLoadWork;
import com.jaydi.ruby.threading.ThreadManager;
import com.jaydi.ruby.threading.Work;

public class NetworkInter {
	private static Rubymine service;

	static {
		Rubymine.Builder builder = new Rubymine.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
		service = builder.build();
	}

	public static boolean isNetworkOnline() {
		boolean status = false;

		try {
			ConnectivityManager cm = (ConnectivityManager) RubyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			} else {
				netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return status;
	}

	public static <T> void getImage(final ImageView imageView, String url, int width, int height) {
		if (url == null || url.isEmpty())
			return;

		StringBuilder sb = new StringBuilder(url);
		if (sb.indexOf("?") != -1)
			sb.append("&width=").append(width).append("&height=").append(height);
		url = sb.toString();
		Bitmap bitmap = Cache.getBitmapItem(url);

		if (imageView != null && bitmap != null)
			imageView.setImageBitmap(bitmap);
		else
			ThreadManager.execute(new ImageLoadWork(url, width, height), new ResponseHandler<Bitmap>() {

				@Override
				protected void onResponse(Bitmap response) {
					if (imageView != null && response != null) {
						Animation fadeIn = AnimationUtils.loadAnimation(RubyApplication.getInstance(), R.anim.fade_in);
						imageView.startAnimation(fadeIn);
						imageView.setImageBitmap(response);
					}
				}

			});
	}

	public static <T> void insertUser(Handler handler, final User user) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.users().insert(user).execute();
			}

		}, handler);
	}

	public static <T> void updateUser(Handler handler, final User user) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.users().update(user).execute();
			}

		}, handler);
	}

	public static <T> void insertUserpair(Handler handler, final Userpair userpair) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.userpairs().insert(userpair).execute();
			}

		}, handler);
	}

	public static <T> void getUserpairs(Handler handler, final long userId) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.userpairs().list(userId).execute();
			}

		}, handler);
	}

	public static <T> void getRubyzones(Handler handler) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.rubyzones().list().execute();
			}

		}, handler);
	}

	public static <T> void getRubymine(Handler handler, final long id) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.rubymines().get(id).execute();
			}

		}, handler);
	}

	public static <T> void getRubymines(Handler handler, final long rubyzoneId) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.rubymines().list(rubyzoneId).execute();
			}

		}, handler);
	}

	public static <T> void getMineGems(Handler handler, final long rubymineId) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.gems().list().mine(rubymineId).execute();
			}

		}, handler);
	}

	public static <T> void getZoneGems(Handler handler, final long rubyzoneId) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.gems().list().zone(rubyzoneId).execute();
			}

		}, handler);
	}

	public static <T> void insertCoupon(Handler handler, final Coupon coupon) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.coupons().insert(coupon).execute();
			}

		}, handler);
	}

	public static <T> void getCoupons(Handler handler, final long userId) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.coupons().list(userId).execute();
			}

		}, handler);
	}

	public static <T> void deleteCoupon(Handler handler, final long id) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.coupons().delete(id).execute();
			}

		}, handler);
	}
}
