package com.jaydi.ruby.connection.network;

import java.io.IOException;

import android.os.Handler;

import com.appspot.ruby_mine.rubymine.Rubymine;
import com.appspot.ruby_mine.rubymine.model.User;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.jaydi.ruby.threading.ThreadManager;
import com.jaydi.ruby.threading.Work;

public class NetworkInter {
	private static Rubymine service;

	static {
		Rubymine.Builder builder = new Rubymine.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
		service = builder.build();
	}

	public static <T> void getRubyzones(Handler handler) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.rubyzones().get().list().execute();
			}

		}, handler);
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

	public static <T> void getRubymine(Handler handler, final long id) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.rubymines().get(id).execute();
			}

		}, handler);
	}
}
